package com.longmenhuarun.init;

import cfbs.api.CFBSMsgUtil;
import com.yjt.cfbs.socket.netty.client.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class NettyClientinit {
        @Value("${CFBS.KEY}")
        private String cfbsKey;

        @Value("${CFBS.MSG.UTIL.DEBUG}")
        private boolean cfbsDebug;

        @Value("${CFBS.SEND.IP}")
        private String cfbsServerIp;

        @Value("${CFBS.SEND.PORT}")
        private int cfbsServerPort;

        @Autowired
        private NettyClient client;

        @PostConstruct
        public void init() throws Exception {

            CFBSMsgUtil.KEY = cfbsKey;
            CFBSMsgUtil.DEBUG = cfbsDebug;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    client.start("56.16.32.195", cfbsServerPort);
                }
            }).start();

        }
    }

