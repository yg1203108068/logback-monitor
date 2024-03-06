package com.javayg.starter.connect;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.javayg.starter.entity.Command;
import com.javayg.starter.entity.Log;
import com.javayg.starter.exception.FixSocketException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
    OutputStream outputStream;
    // 用于输出组件相关的信息
    private final UnsynchronizedAppenderBase<ILoggingEvent> appender;
    // 推送出错是 这里变成 true，并检查原因重新连接
    private boolean fixing = false;

    /**
     * 构造数据发送器
     *
     * @param appender 需要一个 Appender 用于记录启动阶段的信息
     */
    public LogbackSender(UnsynchronizedAppenderBase<ILoggingEvent> appender) {
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
        if (fixing) {
            return;
        }
        try {
            outputStream.write(Command.LOG.getCode());
            outputStream.write(log.payload());
            outputStream.flush();
        } catch (IOException e) {
            fixing = true;
            appender.addWarn("推送 日志数据到 分析平台时出现了异常，日志已丢弃：" + log);
            new Thread(() -> {
                try {
                    fixSocket();
                } catch (IOException | InterruptedException ex) {
                    appender.addWarn("socket 修复失败,无法重建连接");
                    appender.stop();
                } catch (FixSocketException ex) {
                    appender.addWarn(ex.getMessage());
                    appender.stop();
                }
            }).start();
        }
    }

    /**
     * 启动数据发送器
     *
     * @param host 地址
     * @param port 端口
     * @return 返回true表示连接已成功建立，否则表示连接不可用
     * @date 2024/3/5
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
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            // todo 注册： 发送 stater 版本号、服务名称、检查版本是否兼容，服务是否重复
        } catch (IOException e) {
            appender.addError("建立连接失败，请确保服务端已启动 " + e.getMessage());
            return false;
        }

        try {
            InputStream inputStream = socket.getInputStream();
            new Thread(() -> {
                try {
                    byte read = (byte)inputStream.read();
                    if (read == Command.SHUTDOWN.getCode()) {
                        outputStream.write(Command.SHUTDOWN.getCode());
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
            outputStream.write(Command.SHUTDOWN.getCode());
            socket.close();
        } catch (IOException e) {
            appender.addError("关闭连接失败 " + e.getMessage());
        }
    }

    /**
     * 尝试修复连接
     *
     * @date 2024/3/4
     * @author YangGang
     * @description
     */
    private void fixSocket() throws IOException, InterruptedException, FixSocketException {
        boolean unavailable = socket.isOutputShutdown() || socket.isInputShutdown() || (!socket.isConnected()) || (!socket.isClosed()) || (!socket.isBound());
        if (!unavailable) {
            return;
        }
        socket.close();
        for (int i = 0; i < 100; i++) {
            if (initSocket()) {
                fixing = false;
                return;
            }
            Thread.sleep(3000);
        }
        throw new FixSocketException("5分钟内尝试了100次，远程日志分析平台没有连接成功");
    }
}
