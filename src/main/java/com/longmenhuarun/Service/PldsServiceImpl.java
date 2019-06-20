package com.longmenhuarun.Service;

import cfbs.api.CFBSConstant;
import cfbs.api.CFBSMsgUtil;
import com.longmenhuarun.common.Constant;
import com.longmenhuarun.common.FtpUtil;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.common.TimeUtil;
import com.longmenhuarun.entity.BatchLog;
import com.longmenhuarun.entity.TxnPlds;
import com.longmenhuarun.entity.TxnSsxy;
import com.longmenhuarun.entity.UiPlds;
import com.longmenhuarun.enums.JYStatusEnum;
import com.longmenhuarun.model.PldsMsg;
import com.longmenhuarun.repository.BatchLogRepository;
import com.longmenhuarun.repository.TxnPldsRepository;
import com.longmenhuarun.repository.TxnSsxyRepository;
import com.longmenhuarun.repository.UiPldsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/18 17:19
 * 4
 */
@Slf4j
@Service
  public class PldsServiceImpl implements PldsService {
    @Autowired
    BatchLogRepository batchLogRepo;
    @Autowired
    UiPldsRepository uiPldsRepo;
    @Autowired
    TxnPldsRepository txnPldsRepo;
    @Value("${LOCALSEND_FILE_PATH}")
    private String LOCALSEND_FILE_PATH;
    @Value("${LOCALRECV_FILE_PATH}")
    private String LOCALRECV_FILE_PATH;
    @Value("${SEND_FTP_IP}")
    private String SEND_FTP_IP;
    @Value("${SEND_FTP_PORT}")
    private Integer SEND_FTP_PORT;
    @Value("${SEND_FTP_USERNAME}")
    private String SEND_FTP_USERNAME;
    @Value("${SEND_FTP_PASSWORD}")
    private String SEND_FTP_PASSWORD;
    @Value("${SEND_FTP_FILE_PATH}")
    private String SEND_FTP_FILE_PATH;
    @Override
    public PldsMsg anaCustomFile(String filePath, String fileName) {
        PldsMsg pldsMsg = new PldsMsg();
        try {
            BufferedReader breader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + fileName), "UTF-8"));
            String temp = breader.readLine();
            String[] attrs = temp.split("\\|", -1);

            //组文件头
            pldsMsg.setFileName(Constant.prepldsFileName + TimeUtil.getCurDateStr() + MsgUtil.getPlpackNo());
            pldsMsg.setOrgId(Constant.ORGID);
            pldsMsg.setOrigBank(Constant.ORIGBANK);
            pldsMsg.setOrigAcc(Constant.ORIGACC);
            pldsMsg.setOutBank(Constant.bankMap.get(fileName.substring(3,7)));//接收方代码
            pldsMsg.setTranType(Constant.TRANTYPE);//业务种类
            pldsMsg.setMsgType("1202");
            pldsMsg.setCurrency(CFBSConstant.CURRENCY_CNY);
            pldsMsg.setTotalCount(attrs[0]);
            pldsMsg.setTotalAmount(attrs[1]);
            pldsMsg.setCustomLength("0");
            pldsMsg.setCustomContent("");

            while ( (temp=breader.readLine()) !=null) {
                 attrs = temp.split("\\|", -1);
                //组文件体
                PldsMsg.PldsMsgDetail detail = pldsMsg.new PldsMsgDetail();
                detail.setTxnSeq(attrs[0]);//
                detail.setEntrustDate(TimeUtil.getCurDateStr());
                detail.setUserNo(attrs[1]);
                detail.setOrgId("");//发起方分支代码
                detail.setOutAccBank("");//接收方分支代码
                detail.setPayerAcc(attrs[4]);//付款人账号
                detail.setPayerName(attrs[5]);//付款人名称
                detail.setPayerContact("");//付款人联系方式
                detail.setContractId(attrs[10]);//合同协议号
                detail.setAmount(attrs[6]);
                detail.setCharge("0");
                detail.setRemark("");
                detail.setCustomLength("0");
                detail.setCustomContent("");
                pldsMsg.getDetails().add(detail);
            }
            return pldsMsg;
        } catch (IOException e) {
            e.printStackTrace();
        }
      return  pldsMsg;
    }

    @Override
    public String genReqPldsFile(PldsMsg pldsMsg) {
        try {
            //生成明文
            String fileName = PldsMsg.genReqPldsFile(pldsMsg, LOCALSEND_FILE_PATH);
            log.info("明文文件生成"+fileName);
            //生成密文
            String encFileName = CFBSMsgUtil.fileEnc(LOCALSEND_FILE_PATH, fileName);
            log.info("密文文件生成"+encFileName);
            return encFileName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean sendFile(String encFileName) {
        try {
            FTPClient uploadFtpClient = FtpUtil.getConnectedClient(SEND_FTP_IP, SEND_FTP_PORT, SEND_FTP_USERNAME, SEND_FTP_PASSWORD);
            FtpUtil.upload(uploadFtpClient, LOCALSEND_FILE_PATH + encFileName, SEND_FTP_FILE_PATH + encFileName);
            FtpUtil.disconnect(uploadFtpClient);
            log.info("发送批量代收文件成功");
            return true;

        } catch(Exception e) {
            e.printStackTrace();
        }
        log.info("发送批量代收文件失败");
        return false;
    }

    @Override
    public PldsMsg anaRespPldsFile(String respEncFileName) {
        try {
            //解密
            String decFileName = CFBSMsgUtil.fileDec(LOCALRECV_FILE_PATH, respEncFileName);
            log.info("生成回应明文文件"+decFileName);
            //组装对象
           // PldsMsg pldsMsg =  PldsMsg.procRespPldsFile(LOCALSEND_FILE_PATH, decFileName);
           // return pldsMsg;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertDB(PldsMsg pldsMsg,String batchId) {
          //ui_batch_log
        BatchLog batchLog=new BatchLog();
        batchLog.setMsgId(MsgUtil.getReqMsgNo());
        batchLog.setBatchId(batchId);
        batchLog.setOrgId(pldsMsg.getOrgId());
        batchLog.setTotCnt(pldsMsg.getTotalCount());
        batchLog.setTotAmt(pldsMsg.getTotalAmount());
        batchLog.setTxnType(pldsMsg.getTxnType());
        batchLog.setStatus(JYStatusEnum.ANSWER.getCode());
        batchLog.setCreateDate(TimeUtil.getCurDateStr());
        batchLog.setCreateTime(TimeUtil.getCurTimeStr());
        batchLogRepo.save(batchLog);
        //ui_plds
        for (PldsMsg.PldsMsgDetail details:pldsMsg.getDetails()) {
            UiPlds uiPlds = new UiPlds();
            uiPlds.setMsgId(details.getTxnSeq());
            uiPlds.setBatchMsgId(batchLog.getMsgId());
            uiPlds.setTxnType(pldsMsg.getTxnType());
            uiPlds.setOrigBank(pldsMsg.getOrigBank());
            uiPlds.setOrigAcc(pldsMsg.getOrigAcc());
            uiPlds.setOutBank(pldsMsg.getOutBank());
            uiPlds.setPayerAcc(details.getPayerAcc());
            uiPlds.setPayerName(details.getPayerName());
            uiPlds.setTranType(pldsMsg.getTranType());
            uiPlds.setProtNo(details.getContractId());
            uiPlds.setCurrency(pldsMsg.getCurrency());
            uiPlds.setAmount(details.getAmount());
            uiPlds.setCharge(details.getCharge());
            uiPlds.setRemark(details.getRemark());
            uiPlds.setStatus(JYStatusEnum.ANSWER.getCode());
            uiPlds.setCreateDate(TimeUtil.getCurDateStr());
            uiPlds.setCreateTime(TimeUtil.getCurTimeStr());
            uiPldsRepo.save(uiPlds);
            //txn_plds
            TxnPlds txnPlds=new TxnPlds();
            BeanUtils.copyProperties(uiPlds,txnPlds);
            txnPlds.setMsgId(MsgUtil.getReqMsgNo());
            txnPlds.setReqMsgId(uiPlds.getMsgId());
            txnPlds.setEntrustDate(TimeUtil.getCurDateStr());
            txnPldsRepo.save(txnPlds);
        }

    }
}
