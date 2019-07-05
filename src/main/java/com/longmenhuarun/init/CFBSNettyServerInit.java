package com.longmenhuarun.init;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.longmenhuarun.Controller.WebSocket;
import com.longmenhuarun.Service.SsdsService;
import com.longmenhuarun.Service.SsxyService;
import com.longmenhuarun.model.SsdsMsg;
import com.longmenhuarun.model.SsxyMsg;
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

	@Autowired
	WebSocket webSocket;
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
								if(unpackMsg.startsWith(TXNTYPE_SSDS)) {//实时代收
									SsdsMsg ssdsMsg = SsdsMsg.fromMsg(unpackMsg);
									log.info("交易序号:"+ssdsMsg.getReqMsgNo()+"收到实时代收回应报文["+unpackMsg+"]");
									//推送消息
									webSocket.sendMessage(ssdsMsg.getReqMsgNo());
									ssdsService.updateDB(ssdsMsg);
								}
								else if(unpackMsg.startsWith(TXNTYPE_SSXY)){
									SsxyMsg ssxyMsg=SsxyMsg.fromMsg(unpackMsg);
									if("100".equals(ssxyMsg.getProtActType())) {
										log.info("交易序号:" + ssxyMsg.getReqMsgNo() + "收到新增协议回应报文[" + unpackMsg + "]");
									}else {
										log.info("交易序号:" + ssxyMsg.getReqMsgNo() + "收到撤销协议回应报文[" + unpackMsg + "]");
									}
									//推送消息
									webSocket.sendMessage(ssxyMsg.getReqMsgNo());
									ssxyService.updateDB(ssxyMsg);
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
