package com.longmenhuarun.config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.Servlet;

/**
 * @Author: Kingsley
 * @Date: 2019/3/2 15:27
 * @Version 1.0
 */
@Component
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
