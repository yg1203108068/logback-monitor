package com.javayg.log.monitor.common.config.app;

import com.javayg.log.monitor.common.component.ClientCloseCallBack;
import com.javayg.log.monitor.common.component.LogConnect;
import com.javayg.log.monitor.common.component.WebLogRepeater;
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

    @PostConstruct
    public void startServerSocketReceiver() throws IOException {
        socket = new ServerSocket(port);
        new Thread(() -> {

            while (connected) {
                System.out.println(0);
                log.info("检测--》");
                try {
                    Socket connect = socket.accept();
                    LogConnect client = new LogConnect(connect, webLogRepeater, clientCloseCallBack);
                    client.start();
                    clients.add(client);
                } catch (SocketException e) {
                    System.out.println("Socket 已关闭");
                } catch (IOException e) {
                    System.out.println("连接异常，已断开");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @PreDestroy
    public void stopServerSocketReceiver() throws IOException {
        connected = false;
        if (socket != null && !socket.isClosed()) {
            clients.forEach(LogConnect::close);
            socket.close();
            System.out.println("LogbackServerConfig.stopServerSocketReceiver() called 正在关闭客户端连接");
        }
    }
}
