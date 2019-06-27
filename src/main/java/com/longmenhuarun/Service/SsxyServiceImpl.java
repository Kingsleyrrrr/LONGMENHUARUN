package com.longmenhuarun.Service;

import cfbs.api.CFBSMsgUtil;
import com.longmenhuarun.Controller.WebSocket;
import com.longmenhuarun.Vo.SsxyVo;
import com.longmenhuarun.common.Constant;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.common.TimeUtil;
import com.longmenhuarun.entity.InfoSsxy;
import com.longmenhuarun.entity.InfoSxxy;
import com.longmenhuarun.entity.UiSsxy;
import com.longmenhuarun.enums.JYStatusEnum;
import com.longmenhuarun.enums.XYStatusEnum;
import com.longmenhuarun.model.SsxyMsg;
import com.longmenhuarun.repository.InfoSsxyRepository;
import com.longmenhuarun.repository.InfoSxxyRepository;
import com.longmenhuarun.repository.RetcdRepository;
import com.longmenhuarun.repository.UiSsxyRepository;
import com.yjt.cfbs.socket.netty.client.NettyClient;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SsxyServiceImpl implements SsxyService {
    @Autowired
    private NettyClient Client;
    @Autowired
    private UiSsxyRepository uiSsxyRepo;
    @Autowired
    private InfoSsxyRepository infoSsxyRepo;
    @Autowired
    private InfoSxxyRepository infoSxxyRepo;
    @Autowired
    private RetcdRepository retcdRepo;
    @Autowired
    WebSocket webSocket;

    @Override
    public String createSsxyMsg(SsxyMsg ssxyMsg) {
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
        //ui表
        UiSsxy uiSsxy=new UiSsxy();
        BeanUtils.copyProperties(ssxyMsg,uiSsxy);
        uiSsxy.setMsgId(ssxyMsg.getReqMsgNo());
        uiSsxy.setCreateDate(TimeUtil.getCurDateStr());
        uiSsxy.setCreateTime(TimeUtil.getCurTimeStr());
        uiSsxy.setStatus(JYStatusEnum.ANSWER.getCode());
        uiSsxyRepo.save(uiSsxy);
        //info表
        InfoSsxy infoSsxy =new InfoSsxy();
        BeanUtils.copyProperties(uiSsxy, infoSsxy);
        infoSsxy.setMsgId(MsgUtil.getReqMsgNo());
        infoSsxy.setReqMsgId(uiSsxy.getMsgId());
        infoSsxy.setEntrustDate(TimeUtil.getCurDateStr());
        infoSsxyRepo.save(infoSsxy);
    }

    @Override
    @Transactional
    public void updateDB(String ssResMsg) {
        SsxyMsg ssxyMsg=SsxyMsg.fromMsg(ssResMsg);
        //查出txn表
        InfoSsxy infoSsxy = infoSsxyRepo.findByReqMsgId(ssxyMsg.getReqMsgNo());
        BeanUtils.copyProperties(ssxyMsg, infoSsxy);
        //ret_remark
        String retcdremark=retcdRepo.findDescr(infoSsxy.getRetCd());
        infoSsxy.setRetCdRemark(retcdremark);
        infoSsxy.setRspDate(TimeUtil.getCurDateStr());
        infoSsxy.setRspTime(TimeUtil.getCurTimeStr());
        if(Constant.SUCCESSRETCD.contains(infoSsxy.getRetCd())){
                infoSsxy.setStatus(JYStatusEnum.SUCCESS.getCode());
            log.info("请求"+ssxyMsg.getReqMsgNo()+"实时协议生效");
            infoSsxyRepo.save(infoSsxy);
            //插入生效表
            InfoSxxy infoSxxy=new InfoSxxy();
            BeanUtils.copyProperties(infoSsxy, infoSxxy);
            infoSxxy.setActiveDate(TimeUtil.getCurDateStr());
            infoSxxyRepo.save(infoSxxy);
        } else {
            log.error("请求"+ssxyMsg.getReqMsgNo()+"实时协议失败");
            infoSsxy.setStatus(JYStatusEnum.FAIL.getCode());
            infoSsxyRepo.save(infoSsxy);
        }

        //查ui表
        UiSsxy uiSsxy=uiSsxyRepo.findById(ssxyMsg.getReqMsgNo()).orElse(null);
        BeanUtils.copyProperties(infoSsxy,uiSsxy);
        uiSsxy.setMsgId(infoSsxy.getReqMsgId());
        if("S".equals(infoSsxy.getStatus())){
            uiSsxy.setStatus(XYStatusEnum.EFFECTIVE.getCode());
        }
        else {
            uiSsxy.setStatus(XYStatusEnum.ERROR.getCode());
        }
        uiSsxyRepo.save(uiSsxy);
        if("102".equals(ssxyMsg.getProtActType())&&"S".equals(infoSsxy.getStatus())){
            infoSxxyRepo.deleteByprotNo(ssxyMsg.getProtNo());
        }
        //推送消息
        webSocket.sendMessage(ssxyMsg.getReqMsgNo());
    }

    @Override
    public String cancelSsxyMsg(String protNo) {
        //查表
        InfoSxxy infoSxxy=infoSxxyRepo.findByProtNo(protNo);
        //组装报文
        SsxyMsg ssxyMsg=new SsxyMsg();
        BeanUtils.copyProperties(infoSxxy,ssxyMsg);
        ssxyMsg.setReqMsgNo(MsgUtil.getReqMsgNo());
        ssxyMsg.setEntrustDate(TimeUtil.getCurDateStr());
        return ssxyMsg.toDelMsg();
    }

    @Override
    public Page<SsxyVo> findSsxyList(Pageable pageable) {
        Page<InfoSxxy> SxxyPage = infoSxxyRepo.findAll(pageable);
        List<SsxyVo> SsxyVoList = new ArrayList<>();
        for (InfoSxxy infoSxxy : SxxyPage.getContent()) {
            SsxyVo ssxyVo = new SsxyVo();
            BeanUtils.copyProperties(infoSxxy, ssxyVo);
            SsxyVoList.add(ssxyVo);
        }
        return new PageImpl<SsxyVo>(SsxyVoList, pageable, SxxyPage.getTotalElements());

    }

    @Override
    public Page<SsxyVo> findSsxyRecord(Pageable pageable) {
        Page<UiSsxy> UiSsxyPage = uiSsxyRepo.findAll(pageable);
        List<SsxyVo> SsxyVoList = new ArrayList<>();
        for (UiSsxy uiSsxy : UiSsxyPage.getContent()) {
            SsxyVo ssxyVo = new SsxyVo();
            BeanUtils.copyProperties(uiSsxy, ssxyVo);
            ssxyVo.setCreateTime(uiSsxy.getCreateDate()+uiSsxy.getCreateTime());
            SsxyVoList.add(ssxyVo);
        }
        return new PageImpl<SsxyVo>(SsxyVoList, pageable, UiSsxyPage.getTotalElements());

    }

}