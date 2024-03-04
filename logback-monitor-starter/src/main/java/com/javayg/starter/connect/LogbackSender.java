package com.javayg.starter.connect;

import ch.qos.logback.core.spi.ContextAware;
import com.javayg.starter.entity.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 日志事件发送器
 *
 * @author YangGang
 * @date 2024/3/1
 * @description 将收集到的日志事件，依次发送出去
 */
public class LogbackSender {
    private String host;
    private Integer port;
    private Socket socket;
    private boolean enable = false;
    WebLogOutput outputStream;
    // 用于输出组件相关的信息
    private final ContextAware appender;

    /**
     * 构造数据发送器
     */
    public LogbackSender(ContextAware appender) {
        this.appender = appender;
    }

    /**
     * 发送日志数据到服务端
     *
     * @param log 日志对象
     * @date 2024/3/3
     * @author YangGang
     * @description
     */
    public void send(Log log) {
        if (!enable) {
            appender.addWarn("准备推送，但没有可用的Socket，丢弃的日志：" + log.toString());
            return;
        }
        try {
            outputStream.write(log.payload());
        } catch (IOException e) {
            appender.addWarn("推送 日志数据到 分析平台时出现了异常，日志已丢弃：" + log);
        }
    }

    /**
     * 启动数据发送器
     *
     * @return 返回true表示连接已成功建立，否则表示连接不可用
     * @date 2024/3/3
     * @author YangGang
     * @description 建立与服务端的连接
     */
    public boolean start(String host, Integer port) {
        appender.addInfo("启动日志推送器");
        enable = (host != null && port != null);
        if (enable) {
            this.host = host;
            this.port = port;
            return initSocket();
        }
        // 缺少配置项，提示
        appender.addWarn("请检查接收方配置：logback.monitor.host 和 logback.monitor.port");
        return false;
    }

    public boolean initSocket() {
        appender.addInfo("初始化Socket");
        // 初始化连接
        try {
            socket = new Socket("localhost", 8733);
            outputStream = new WebLogOutput(socket.getOutputStream());
        } catch (IOException e) {
            appender.addError("建立连接失败，请确保服务端已启动 " + e.getMessage());
            return false;
        }

        try {
            InputStream inputStream = socket.getInputStream();
            new Thread(() -> {
                try {
                    byte[] bytes = new byte[1024];
                    int len = inputStream.read(bytes);
                    byte[] strBytes = new byte[len];
                    System.arraycopy(bytes, 0, strBytes, 0, len);
                    String fromServer = new String(strBytes, StandardCharsets.UTF_8);
                    System.out.println("fromServer=>" + fromServer);
                    if (fromServer.equals("SHUTDOWN")) {
                        outputStream.write(0);
                        socket.close();
                    }
                } catch (IOException e) {
                    appender.addError("与日志监控服务器的连接出现异常 " + e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            appender.addError("建立的连接输入流存在异常 " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 关闭数据发送器
     *
     * @date 2024/3/3
     * @author YangGang
     * @description 断开与服务端的连接
     */
    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            appender.addError("关闭连接失败 " + e.getMessage());
        }
    }
}
