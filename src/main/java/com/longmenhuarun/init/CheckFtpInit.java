package com.longmenhuarun.init;

import com.longmenhuarun.Service.PldsService;
import com.longmenhuarun.common.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/19 15:36
 * 4
 */
@Slf4j
public class CheckFtpInit {
    @Value("${LOCAL_FILE_PATH}")
    private String LOCAL_FILE_PATH;
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
    @PostConstruct
    public void init() throws Exception {

        new Thread(new Runnable() {

            @Override
            public void run() {

                Timer timer = new Timer(true);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                          log.info("开始访问ftp");
                        try {

                            FTPClient ftpClient = FtpUtil.getConnectedClient(RECV_FTP_IP, RECV_FTP_PORT, RECV_FTP_USERNAME, RECV_FTP_PASSWORD);

                            boolean isChangePath = ftpClient.changeWorkingDirectory(RECV_FTP_FILE_PATH);
                            if(isChangePath) {
                                FTPFile[] files = ftpClient.listFiles();
                                for(FTPFile file : files) {

                                    boolean isDownload = FtpUtil.download(ftpClient, file.getName(), LOCAL_FILE_PATH + file.getName());
                                    if(isDownload) {

                                        System.out.println("收到代收付回应密文文件！[" + file.getName() + "]");
                                        pldsService.anaRespPldsFile(file.getName());
                                        //更新数据库
                                        //处理成功后删除FTP文件，以免重复处理
                                        ftpClient.deleteFile(file.getName());
                                    }
                                }
                            }

                            FtpUtil.disconnect(ftpClient);

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, 0, 3000);
            }
        }).start();
    }
}
