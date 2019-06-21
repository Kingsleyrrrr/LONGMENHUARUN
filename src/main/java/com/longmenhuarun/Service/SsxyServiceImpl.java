package com.longmenhuarun.Service;

import cfbs.api.CFBSMsgUtil;
import com.longmenhuarun.Controller.WebSocket;
import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.Vo.SsxyVo;
import com.longmenhuarun.common.Constant;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.common.TimeUtil;
import com.longmenhuarun.entity.TxnSsds;
import com.longmenhuarun.entity.TxnSsxy;
import com.longmenhuarun.entity.UiSsds;
import com.longmenhuarun.entity.UiSsxy;
import com.longmenhuarun.enums.JYStatusEnum;
import com.longmenhuarun.enums.XYStatusEnum;
import com.longmenhuarun.model.SsdsMsg;
import com.longmenhuarun.model.SsxyMsg;
import com.longmenhuarun.repository.RetcdRepository;
import com.longmenhuarun.repository.TxnSsxyRepository;
import com.longmenhuarun.repository.UiSsxyRepository;
import com.yjt.cfbs.socket.netty.client.NettyClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SsxyServiceImpl implements SsxyService {
    @Autowired
    private NettyClient Client;
    @Autowired
    private UiSsxyRepository uiSsxyRepo;
    @Autowired
    private TxnSsxyRepository txnSsxyRepo;
    @Autowired
    private RetcdRepository retcdRepo;
    @Autowired
    WebSocket webSocket;

    @Override
    public String createSsxyMsg(SsxyMsg ssxyMsg) {
        ssxyMsg.setReqMsgNo(MsgUtil.getReqMsgNo());
        ssxyMsg.setEntrustDate(TimeUtil.getCurDateStr());
        ssxyMsg.setOrgId(Constant.ORGID);
        ssxyMsg.setProtNo(Constant.getProtNoPre + ssxyMsg.getPayerBank().substring(0, 3) + ssxyMsg.getPayerAcc());
        ssxyMsg.setTranType(Constant.TRANTYPE);
        return ssxyMsg.toAddMsg();
    }

    @Override
    public boolean sendSsxyMsg(String ssReqMsg) {
        try {
            Client.send(CFBSMsgUtil.packCfbsReqMsg(ssReqMsg));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void insertDB(String ssReqMsg) {
        SsxyMsg ssxyMsg= SsxyMsg.fromMsg(ssReqMsg);
        UiSsxy uiSsxy=new UiSsxy();
        BeanUtils.copyProperties(ssxyMsg,uiSsxy);
        uiSsxy.setMsgId(ssxyMsg.getReqMsgNo());
        uiSsxy.setCreateDate(TimeUtil.getCurDateStr());
        uiSsxy.setCreateTime(TimeUtil.getCurTimeStr());
        uiSsxy.setStatus(JYStatusEnum.ANSWER.getCode());
        if("100".equals(uiSsxy.getProtActType())){
            uiSsxy.setProtActType("ADD");
        }else if("102".equals(uiSsxy.getProtActType())){
            uiSsxy.setProtActType("DEL");
        }
        uiSsxyRepo.save(uiSsxy);
        //txn表
        TxnSsxy txnSsxy=new TxnSsxy();
        BeanUtils.copyProperties(uiSsxy,txnSsxy);
        txnSsxy.setMsgId(MsgUtil.getReqMsgNo());
        txnSsxy.setReqMsgId(uiSsxy.getMsgId());
        txnSsxy.setEntrustDate(TimeUtil.getCurDateStr());
        txnSsxyRepo.save(txnSsxy);
    }
    @Override
    @Transactional
    public void updateDB(String ssResMsg) {
        SsxyMsg ssxyMsg=SsxyMsg.fromMsg(ssResMsg);
        //查出txn表
        TxnSsxy txnSsxy= txnSsxyRepo.findByReqMsgId(ssxyMsg.getReqMsgNo());
        BeanUtils.copyProperties(ssxyMsg,txnSsxy);
        //ret_remark
        String retcdremark=retcdRepo.findDescr(txnSsxy.getRetCd());
        txnSsxy.setRetCdRemark(retcdremark);
        if(Constant.SUCCESSRETCD.contains(txnSsxy.getRetCd())){
                txnSsxy.setStatus(JYStatusEnum.SUCCESS.getCode());
        } else {
            txnSsxy.setStatus(JYStatusEnum.FAIL.getCode());
        }
        txnSsxy.setRspDate(TimeUtil.getCurDateStr());
        txnSsxy.setRspTime(TimeUtil.getCurTimeStr());
        txnSsxyRepo.save(txnSsxy);
        //查ui表
        UiSsxy uiSsxy=uiSsxyRepo.findById(ssxyMsg.getReqMsgNo()).orElse(null);
        BeanUtils.copyProperties(txnSsxy,uiSsxy);
        uiSsxy.setMsgId(txnSsxy.getReqMsgId());
        if("S".equals(txnSsxy.getStatus())){
            uiSsxy.setStatus(XYStatusEnum.EFFECTIVE.getCode());
        }
        else {
            uiSsxy.setStatus(XYStatusEnum.ERROR.getCode());
        }
        uiSsxyRepo.save(uiSsxy);
        if("102".equals(ssxyMsg.getProtActType())&&"S".equals(txnSsxy.getStatus())){
            uiSsxyRepo.cancelSsxyByprotNo(ssxyMsg.getProtNo());
        }
        //推送消息
        webSocket.sendMessage(ssxyMsg.getReqMsgNo());
    }

    @Override
    public String cancelSsxyMsg(String msgId) {
        //查表
        UiSsxy uiSsxy=uiSsxyRepo.findById(msgId).orElse(null);
        //组装报文
        SsxyMsg ssxyMsg=new SsxyMsg();
        BeanUtils.copyProperties(uiSsxy,ssxyMsg);
        ssxyMsg.setReqMsgNo(MsgUtil.getReqMsgNo());
        ssxyMsg.setEntrustDate(TimeUtil.getCurDateStr());
        return ssxyMsg.toDelMsg();
    }

    @Override
    public Page<SsxyVo> findSsxyList(Pageable pageable) {
        Page<UiSsxy> UiSsxyPage = uiSsxyRepo.findAll(pageable);
        List<SsxyVo> SsxyVoList = new ArrayList<>();
        for (UiSsxy uiSsxy : UiSsxyPage.getContent()) {
            SsxyVo ssxyVo = new SsxyVo();
            BeanUtils.copyProperties(uiSsxy, ssxyVo);
            ssxyVo.setCreateTime(uiSsxy.getCreateDate()+uiSsxy.getCreateTime());
            if("100".equals(ssxyVo.getProtActType()))
            SsxyVoList.add(ssxyVo);
        }
        return new PageImpl<SsxyVo>(SsxyVoList, pageable, UiSsxyPage.getTotalElements());

    }

}