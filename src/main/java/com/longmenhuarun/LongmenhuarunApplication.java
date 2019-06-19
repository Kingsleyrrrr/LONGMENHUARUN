package com.longmenhuarun;

import com.yjt.cfbs.socket.netty.client.NettyClient;
import com.yjt.cfbs.socket.netty.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({ NettyClient.class, NettyServer.class })
@SpringBootApplication
public class LongmenhuarunApplication {

    public static void main(String[] args) {
        SpringApplication.run(LongmenhuarunApplication.class, args);
    }

}
