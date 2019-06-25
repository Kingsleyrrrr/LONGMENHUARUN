package com.longmenhuarun.Service;

import cfbs.api.CFBSMsgUtil;
import com.longmenhuarun.Controller.WebSocket;
import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.common.Constant;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.common.TimeUtil;
import com.longmenhuarun.entity.InfoSsxy;
import com.longmenhuarun.entity.InfoSxxy;
import com.longmenhuarun.entity.TxnSsds;
import com.longmenhuarun.entity.UiSsds;
import com.longmenhuarun.enums.DZStatusEnum;
import com.longmenhuarun.enums.JYStatusEnum;
import com.longmenhuarun.model.DzMsg;
import com.longmenhuarun.model.PldsMsg;
import com.longmenhuarun.model.SsdsMsg;
import com.longmenhuarun.repository.InfoSxxyRepository;
import com.longmenhuarun.repository.RetcdRepository;
import com.longmenhuarun.repository.TxnSsdsRepository;
import com.longmenhuarun.repository.UiSsdsRepository;
import com.sun.media.jfxmedia.logging.Logger;
import com.yjt.cfbs.socket.netty.client.NettyClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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
    private InfoSxxyRepository infoSxxyRepo;
    @Autowired
    WebSocket webSocket;
    @Value("${LOCALRECV_FILE_PATH}")
    private String LOCALRECV_FILE_PATH;

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
    @Transactional
    public void insertDB(String ssReqMsg) {
        SsdsMsg ssdsMsg = SsdsMsg.fromMsg(ssReqMsg);
        //ui表
        UiSsds uiSsds = new UiSsds();
        BeanUtils.copyProperties(ssdsMsg, uiSsds);
        uiSsds.setMsgId(ssdsMsg.getReqMsgNo());
        uiSsds.setCreateDate(TimeUtil.getCurDateStr());
        uiSsds.setCreateTime(TimeUtil.getCurTimeStr());
        uiSsds.setStatus(JYStatusEnum.ANSWER.getCode());
        uiSsds.setProtNo(ssdsMsg.getProtocolNo());
        uiSsds.setEntrustDate(TimeUtil.getCurDateStr());
        uiSsdsRepo.save(uiSsds);
        //txn表
        TxnSsds txnSsds = new TxnSsds();
        BeanUtils.copyProperties(uiSsds, txnSsds);
        txnSsds.setMsgId(MsgUtil.getReqMsgNo());
        txnSsds.setReqMsgId(uiSsds.getMsgId());
        txnSsdsRepo.save(txnSsds);
    }

    @Override
    @Transactional
    public void updateDB(String ssResMsg) {
        SsdsMsg ssdsMsg = SsdsMsg.fromMsg(ssResMsg);
        //查出txn表
        TxnSsds txnSsds = txnSsdsRepo.findByReqMsgId(ssdsMsg.getReqMsgNo());
        //怕附言没了
        ssdsMsg.setRemark(txnSsds.getRemark());
        BeanUtils.copyProperties(ssdsMsg, txnSsds);
        //ret_remark
        String retcdremark = retcdRepo.findDescr(txnSsds.getRetCd());
        txnSsds.setRetCdRemark(retcdremark);
        //成功
        if (Constant.SUCCESSRETCD.contains(txnSsds.getRetCd())) {
            txnSsds.setStatus(JYStatusEnum.SUCCESS.getCode());
            log.info("请求" + ssdsMsg.getReqMsgNo() + "实时代收成功");
        } else {
            txnSsds.setStatus(JYStatusEnum.FAIL.getCode());
            log.error("请求" + ssdsMsg.getReqMsgNo() + "实时代收失败");
        }
        txnSsds.setDzFlag(DZStatusEnum.CHECK.getCode());
        txnSsds.setRspDate(TimeUtil.getCurDateStr());
        txnSsds.setRspTime(TimeUtil.getCurTimeStr());
        txnSsdsRepo.save(txnSsds);
        //查ui表
        UiSsds uiSsds = uiSsdsRepo.findById(ssdsMsg.getReqMsgNo()).orElse(null);
        BeanUtils.copyProperties(txnSsds, uiSsds);
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
            ssdsVo.setCreateTime(uiSsds.getCreateDate() + uiSsds.getCreateTime());
            SsdsVoList.add(ssdsVo);
        }
        return new PageImpl<SsdsVo>(SsdsVoList, pageable, UiSsdsPage.getTotalElements());

    }

    @Override
    public void dz(String filename) {
        try {
            //解密
            String decFileName = CFBSMsgUtil.fileDec(LOCALRECV_FILE_PATH, filename);
            //组装对象
            List<DzMsg> dzMsgList = DzMsg.procDzFile(LOCALRECV_FILE_PATH, decFileName);
            for (DzMsg dz : dzMsgList) {
                TxnSsds txnSsds = txnSsdsRepo.findByReqMsgId(dz.getMsgId());
                if (txnSsds != null && txnSsds.getAmount().equals(dz.getAmount())) {
                    txnSsds.setDzFlag(DZStatusEnum.RECONCILED.getCode());
                    txnSsds.setDzDate(TimeUtil.getCurDateStr());
                    txnSsdsRepo.save(txnSsds);
                }
                UiSsds uiSsds = uiSsdsRepo.findById(dz.getMsgId()).orElse(null);
                if (uiSsds != null && uiSsds.getAmount().equals(dz.getAmount())) {
                    uiSsds.setDzFlag(DZStatusEnum.RECONCILED.getCode());
                    uiSsds.setDzDate(TimeUtil.getCurDateStr());
                    uiSsdsRepo.save(uiSsds);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkProtocol(String ssReqMsg) {
        SsdsMsg ssdsMsg = SsdsMsg.fromMsg(ssReqMsg);
        InfoSxxy infoSxxy = infoSxxyRepo.checkProt(ssdsMsg.getOrgId(), ssdsMsg.getTranType(), ssdsMsg.getUserNo());
        if(ssdsMsg.getProtocolNo().equals(infoSxxy.getProtNo())){
            return true;
        }
        return false;
    }
}