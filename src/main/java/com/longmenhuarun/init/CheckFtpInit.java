package com.longmenhuarun.init;

import com.longmenhuarun.Service.PldsService;
import com.longmenhuarun.Service.SsdsService;
import com.longmenhuarun.common.FtpUtil;
import com.longmenhuarun.model.PldsMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/19 15:36
 * 4
 */
@Slf4j
@Component
public class CheckFtpInit {
    @Value("${LOCALRECV_FILE_PATH}")
    private String LOCALRECV_FILE_PATH;
    @Value("${RECV_FTP_IP}")
    private String RECV_FTP_IP;
    @Value("${RECV_FTP_PORT}")
    private Integer RECV_FTP_PORT;
    @Value("${RECV_FTP_USERNAME}")
    private String RECV_FTP_USERNAME;
    @Value("${RECV_FTP_PASSWORD}")
    private String RECV_FTP_PASSWORD;
    @Value("${RECV_FTP_FILE_PATH}")
    private String RECV_FTP_FILE_PATH;
    @Autowired
    PldsService pldsService;
    @Autowired
    SsdsService ssdsService;
    @PostConstruct
    public void init() throws Exception {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Timer timer = new Timer(true);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        try {
                            log.info("访问ftp");
                            FTPClient ftpClient = FtpUtil.getConnectedClient(RECV_FTP_IP, RECV_FTP_PORT, RECV_FTP_USERNAME, RECV_FTP_PASSWORD);
                            boolean isChangePath = ftpClient.changeWorkingDirectory(RECV_FTP_FILE_PATH);
                            if(isChangePath) {
                                FTPFile[] files = ftpClient.listFiles();
                                for(FTPFile file : files) {

                                    boolean isDownload = FtpUtil.download(ftpClient, file.getName(), LOCALRECV_FILE_PATH + file.getName());
                                    if(isDownload) {
                                        String filename=file.getName();
                                        if(filename.startsWith("DTLTYP")){
                                            log.info("收到代收付对账文件！[" + filename + "]");
                                           ssdsService.dz(filename);
                                            //生成机构对账文件
                                          //  ssdsService.genCustomFile(pldsMsg, pldsMsg.getRefFileName());
                                        }else {
                                            log.info("收到批量回应密文文件！[" + filename + "]");
                                            PldsMsg pldsMsg = pldsService.anaRespPldsFile(filename);
                                            //更新数据库
                                            pldsService.updateDB(pldsMsg);
                                            //生成客户化回应文件
                                            pldsService.genCustomFile(pldsMsg, pldsMsg.getRefFileName());
                                        }
                                            //处理成功后删除FTP文件，以免重复处理
                                            ftpClient.deleteFile(filename);
                                    }
                                }
                            }

                            FtpUtil.disconnect(ftpClient);

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, 0, 2*60*1000);
            }
        }).start();
    }
}
