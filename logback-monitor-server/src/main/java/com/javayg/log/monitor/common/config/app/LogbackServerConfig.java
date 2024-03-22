package com.javayg.log.monitor.common.config.app;

import com.javayg.log.monitor.common.component.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Configuration
public class LogbackServerConfig {
    @Value("${logServer.port}")
    private int port;
    @Autowired
    private WebLogRepeater webLogRepeater;
    ServerSocket socket;
    List<LogConnect> clients = new LinkedList<>();
    private final ClientCloseCallBack clientCloseCallBack = client -> clients.remove(client);
    boolean connected = true;
    @Autowired
    LogParsing resolve;
    @Autowired
    ModuleManager moduleManager;

    @PostConstruct
    public void startServerSocketReceiver() throws IOException {
        socket = new ServerSocket(port);
        new Thread(() -> {

            while (connected) {
                log.info("等待客户端日志推送器连接...");
                try {
                    Socket connect = socket.accept();
                    LogConnect client = new LogConnect(connect, webLogRepeater, clientCloseCallBack, resolve,moduleManager);
                    client.start();
                    clients.add(client);
                } catch (SocketException e) {
                    log.warn("连接被关闭,{}", e.getMessage());
                } catch (IOException e) {
                    log.error("LogbackServerConfig.startServerSocketReceiver() -连接异常，已断开- ", e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @PreDestroy
    public void stopServerSocketReceiver() throws IOException {
        log.info("Shutdown LogbackServer...");
        connected = false;
        if (socket != null && !socket.isClosed()) {
            clients.forEach(LogConnect::close);
            socket.close();
            log.info("Shutdown LogbackServer.");
        }
    }
}
