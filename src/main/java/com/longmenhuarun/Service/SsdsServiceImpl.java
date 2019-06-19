package com.longmenhuarun.Service;

import cfbs.api.CFBSMsgUtil;
import com.longmenhuarun.Controller.WebSocket;
import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.common.Constant;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.common.TimeUtil;
import com.longmenhuarun.entity.TxnSsds;
import com.longmenhuarun.entity.UiSsds;
import com.longmenhuarun.enums.DZStatusEnum;
import com.longmenhuarun.enums.JYStatusEnum;
import com.longmenhuarun.model.SsdsMsg;
import com.longmenhuarun.repository.RetcdRepository;
import com.longmenhuarun.repository.TxnSsdsRepository;
import com.longmenhuarun.repository.UiSsdsRepository;
import com.yjt.cfbs.socket.netty.client.NettyClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class SsdsServiceImpl implements SsdsService {
    @Autowired
    private NettyClient Client;
    @Autowired
    private UiSsdsRepository uiSsdsRepo;
    @Autowired
    private TxnSsdsRepository txnSsdsRepo;
    @Autowired
    private RetcdRepository retcdRepo;
    @Autowired
    WebSocket webSocket;
    @Override
    public String createSsdsMsg(SsdsMsg ssdsMsg) {
        ssdsMsg.setEntrustDate(TimeUtil.getCurDateStr());
        ssdsMsg.setOrgId(Constant.ORGID);
        ssdsMsg.setOrigBank(Constant.ORIGBANK);
        ssdsMsg.setOrigAcc(Constant.ORIGACC);
        ssdsMsg.setTranType(Constant.TRANTYPE);
        ssdsMsg.setProtocolNo(Constant.getProtNoPre + ssdsMsg.getOutBank().substring(0, 3) + ssdsMsg.getPayerAcc());
        return ssdsMsg.toMsg();
    }

    @Override
    public boolean sendSsdsMsg(String ssReqMsg) {
        try {
            Client.send(CFBSMsgUtil.packCfbsReqMsg(ssReqMsg));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void insertDB(String ssReqMsg) {
        SsdsMsg ssdsMsg= SsdsMsg.fromMsg(ssReqMsg);
        UiSsds uiSsds=new UiSsds();
        BeanUtils.copyProperties(ssdsMsg,uiSsds);
        uiSsds.setMsgId(ssdsMsg.getReqMsgNo());
        uiSsds.setCreateDate(TimeUtil.getCurDateStr());
        uiSsds.setCreateTime(TimeUtil.getCurTimeStr());
        uiSsds.setStatus(JYStatusEnum.ANSWER.getCode());
        uiSsds.setProtNo(ssdsMsg.getProtocolNo());
        uiSsds.setEntrustDate(TimeUtil.getCurDateStr());
        uiSsdsRepo.save(uiSsds);
        //txn表
        TxnSsds txnSsds=new TxnSsds();
        BeanUtils.copyProperties(uiSsds,txnSsds);
        txnSsds.setMsgId(MsgUtil.getReqMsgNo());
        txnSsds.setReqMsgId(uiSsds.getMsgId());
        txnSsdsRepo.save(txnSsds);
    }

    @Override
    public void updateDB(String ssResMsg) {
        SsdsMsg ssdsMsg=SsdsMsg.fromMsg(ssResMsg);
        //查出txn表
       TxnSsds txnSsds= txnSsdsRepo.findByReqMsgId(ssdsMsg.getReqMsgNo());
       //怕附言没了
       ssdsMsg.setRemark(txnSsds.getRemark());
       BeanUtils.copyProperties(ssdsMsg,txnSsds);
       //ret_remark
        String retcdremark=retcdRepo.findDescr(txnSsds.getRetCd());
        txnSsds.setRetCdRemark(retcdremark);
        if(Constant.SUCCESSRETCD.contains(txnSsds.getRetCd())){
            txnSsds.setStatus(JYStatusEnum.SUCCESS.getCode());
        } else {
            txnSsds.setStatus(JYStatusEnum.FAIL.getCode());
        }
        txnSsds.setDzFlag(DZStatusEnum.CHECK.getCode());
        txnSsds.setDzDate(TimeUtil.getCurDateStr());
        txnSsds.setRspDate(TimeUtil.getCurDateStr());
        txnSsds.setRspTime(TimeUtil.getCurTimeStr());
        txnSsdsRepo.save(txnSsds);
        //查ui表
        UiSsds uiSsds=uiSsdsRepo.findById(ssdsMsg.getReqMsgNo()).orElse(null);
        BeanUtils.copyProperties(txnSsds,uiSsds);
        uiSsds.setMsgId(txnSsds.getReqMsgId());
        uiSsdsRepo.save(uiSsds);
        //推送消息
        webSocket.sendMessage(ssdsMsg.getReqMsgNo());
    }

    @Override
    public Page<SsdsVo> findSsdsList(Pageable pageable) {
        Page<UiSsds> UiSsdsPage = uiSsdsRepo.findAll(pageable);
        List<SsdsVo> SsdsVoList = new ArrayList<>();
        for (UiSsds uiSsds : UiSsdsPage.getContent()) {
            SsdsVo ssdsVo = new SsdsVo();
            BeanUtils.copyProperties(uiSsds, ssdsVo);
            ssdsVo.setReqMsgNo(uiSsds.getMsgId());
            ssdsVo.setCreateTime(uiSsds.getCreateDate()+uiSsds.getCreateTime());
            SsdsVoList.add(ssdsVo);
        }
        return new PageImpl<SsdsVo>(SsdsVoList, pageable, UiSsdsPage.getTotalElements());

    }
}