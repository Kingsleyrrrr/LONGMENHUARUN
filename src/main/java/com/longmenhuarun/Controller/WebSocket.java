package com.longmenhuarun.Controller;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/11 10:30
 * 4
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArrayList;
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {
    private Session session;
    private static CopyOnWriteArrayList<WebSocket>webSocketSet=new CopyOnWriteArrayList<>();
    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        webSocketSet.add(this);
        log.info("[websocket]有新的连接,总数{}",webSocketSet.size());
    }
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        log.info("[websocket]连接断开,总数{}",webSocketSet.size());
    }
    @OnMessage
    public void onMessage(String message){
        log.info("收到消息{}",message);

    }
    public void sendMessage(String message){
        for (WebSocket webSocket:webSocketSet){
            log.info("广播消息={}",message);
            try{
                webSocket.session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
