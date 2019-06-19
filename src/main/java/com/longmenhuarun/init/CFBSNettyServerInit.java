package com.longmenhuarun.init;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.longmenhuarun.Controller.WebSocket;
import com.longmenhuarun.Service.SsdsService;
import com.longmenhuarun.Service.SsxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yjt.cfbs.socket.netty.common.CFBSNettyChannelListener;
import com.yjt.cfbs.socket.netty.server.NettyServer;

import cfbs.api.CFBSMsgUtil;

import static cfbs.api.CFBSConstant.*;

@Slf4j
@Component
public class CFBSNettyServerInit {
	@Autowired
	SsdsService ssdsService;
	@Autowired
	SsxyService ssxyService;

	@Value("${CFBS.RECV.PORT}")
	private int cfbsRecvPort;
	
	@Resource
	private NettyServer server;

	@PostConstruct
	public void init() throws Exception {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					server.start(cfbsRecvPort, new CFBSNettyChannelListener() {
						public void processRead(byte[] msg) {
							
							try {

					    		String unpackMsg = CFBSMsgUtil.unpackCfbsRspMsg(msg);
								log.info("回应报文["+unpackMsg+"]");
								if(unpackMsg.startsWith(TXNTYPE_SSDS)) {//实时代收
									ssdsService.updateDB(unpackMsg);
								}
								else if(unpackMsg.startsWith(TXNTYPE_SSXY)){
									ssxyService.updateDB(unpackMsg);
								}
					    	} catch(Exception e) {
					    		e.printStackTrace();
					    	}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
}
