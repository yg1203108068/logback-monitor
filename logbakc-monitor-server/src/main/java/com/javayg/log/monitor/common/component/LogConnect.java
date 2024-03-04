package com.javayg.log.monitor.common.component;

import cn.hutool.core.lang.Assert;
import com.javayg.log.monitor.common.constant.Command;
import com.javayg.log.monitor.common.entity.log.Log;
import com.javayg.log.monitor.common.exception.LogParserException;
import com.javayg.log.monitor.common.exception.UnknownLogLevelException;
import com.javayg.log.monitor.common.utils.ByteUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * 日志连接类
 *
 * @author YangGang
 * @date 2024/2/26
 * @description 用于接管远程客户端。
 */
@Slf4j
public class LogConnect extends Thread {

    private final WebLogRepeater webLogRepeater;
    private final Socket remote;
    private boolean connected = true;
    private final ClientCloseCallBack clientCloseCallBack;
    private final LogParsing resolve;
    private boolean disconnecting = false;

    public LogConnect(Socket remote, WebLogRepeater webLogRepeater, ClientCloseCallBack clientCloseCallBack, LogParsing resolve) {
        this.remote = remote;
        this.webLogRepeater = webLogRepeater;
        this.clientCloseCallBack = clientCloseCallBack;
        this.resolve = resolve;
        log.info("有新的 日志推送终端 接入");
    }

    @Override
    public void run() {
        try {
            super.run();
            // 不用向客户端发送消息
            while (connected) {
                log.info("循环读取客户端发送来的日志信息");
                // 循环读取客户端发送来的日志信息
                InputStream inputStream = remote.getInputStream();
                byte command = (byte)inputStream.read();
                try {
                    log.info("尝试处理日志信息，命令：{}", command);
                    // 处理关闭
                    if (command == Command.SHUTDOWN.getCode()) {
                        if (disconnecting) {
                            remote.close();
                            break;
                        }
                        disconnecting = true;
                        webLogRepeater.write(Command.SHUTDOWN.getCode());
                    } else if (command == Command.LOG.getCode()) {
                        log.info("接收到新的日志数据消息");
                        // 获取数据包长度
                        byte[] bytes = new byte[4];
                        int headerReadLen = inputStream.read(bytes);
                        Assert.equals(headerReadLen, 4, () -> new LogParserException("日志的头信息获取失败"));
                        int payloadLen = ByteUtils.byteArrayToInt(bytes);
                        byte[] payloadBytes = new byte[payloadLen];
                        // 获取负载
                        int payloadReadLen = inputStream.read(payloadBytes);
                        Assert.equals(payloadReadLen, payloadLen, () -> new LogParserException("日志的负载获取失败"));
                        ByteBuffer payloadBuffer = ByteBuffer.wrap(payloadBytes);

                        // 将客户端信息处理后发送给日志中继器
                        Log logInfo = resolve.resolve(payloadBuffer);
                        webLogRepeater.logHandler(logInfo);
                        log.info("日志已处理");
                    } else if (command == Command.CLIENT_CLOSE.getCode()) {
                        webLogRepeater.warn("客户端已断开连接");
                        break;
                    }
                } catch (LogParserException | UnknownLogLevelException e) {
                    log.error("LogConnect.run() -日志解析异常- ", e);
                    webLogRepeater.error("日志解析异常");
                }
            }
        } catch (SocketException e) {
            System.out.println("已经与 客户端 Socket 断开连接");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        System.out.println("服务端主动关闭连接");
        try {
            remote.getOutputStream().write(Command.SHUTDOWN.getCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connected = false;
        try {
            remote.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientCloseCallBack.close(this);
    }
}