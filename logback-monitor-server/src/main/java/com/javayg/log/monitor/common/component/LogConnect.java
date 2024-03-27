package com.javayg.log.monitor.common.component;

import cn.hutool.core.lang.Assert;
import com.javayg.log.monitor.common.constant.Command;
import com.javayg.log.monitor.common.entity.log.Log;
import com.javayg.log.monitor.common.entity.net.RegistrationParams;
import com.javayg.log.monitor.common.entity.net.Response;
import com.javayg.log.monitor.common.entity.net.Status;
import com.javayg.log.monitor.common.entity.net.wapper.VariableLengthString;
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
    private final ModuleManager moduleManager;
    private int serverId;

    public LogConnect(Socket remote, WebLogRepeater webLogRepeater, ClientCloseCallBack clientCloseCallBack, LogParsing resolve, ModuleManager moduleManager) {
        this.remote = remote;
        this.webLogRepeater = webLogRepeater;
        this.clientCloseCallBack = clientCloseCallBack;
        this.resolve = resolve;
        this.moduleManager = moduleManager;
        log.info("有新的 日志推送终端 接入");
    }

    @Override
    public void run() {
        try {
            try {
                super.run();
                // 不用向客户端发送消息
                while (connected) {
                    log.debug("循环读取客户端发送来的日志信息");
                    // 循环读取客户端发送来的日志信息
                    InputStream inputStream = remote.getInputStream();
                    byte command = (byte) inputStream.read();
                    try {
                        log.debug("尝试处理日志信息，命令：{}", command);
                        // 处理关闭
                        if (command == Command.SHUTDOWN.getCode()) {
                            shutdownStarter();
                            break;
                        } else if (command == Command.LOG.getCode()) {
                            log.debug("接收到新的日志数据消息,serverId={}", serverId);
                            // 获取数据包长度
                            byte[] bytes = new byte[4];
                            int headerReadLen = inputStream.read(bytes);
                            Assert.equals(headerReadLen, 4, () -> new LogParserException("日志的头信息获取失败,数据包错误"));
                            int payloadLen = ByteUtils.byteArrayToInt(bytes);
                            byte[] payloadBytes = new byte[payloadLen];
                            // 获取负载
                            int payloadReadLen = inputStream.read(payloadBytes);
                            Assert.equals(payloadReadLen, payloadLen, () -> new LogParserException("日志的负载获取失败，数据包错误"));
                            ByteBuffer payloadBuffer = ByteBuffer.wrap(payloadBytes);

                            // 将客户端信息处理后发送给日志中继器
                            Log logInfo = resolve.resolve(payloadBuffer);
                            webLogRepeater.logHandler(logInfo, serverId);
                            log.debug("日志已处理");
                        } else if (command == Command.CLIENT_CLOSE.getCode()) {
                            webLogRepeater.warn("客户端已断开连接");
                            break;
                        } else if (command == Command.REGISTER.getCode()) {
                            RegistrationParams registrationParams = new RegistrationParams(inputStream);
                            byte protocolVersion = registrationParams.getProtocolVersion();
                            if (protocolVersion > 2) {
                                webLogRepeater.write(Command.REGISTER.getCode());
                                Response response = new Response();
                                response.setMsg(new VariableLengthString("未知的版本"));
                                response.setStatus(Status.ERROR);
                                webLogRepeater.write(response.getPayload());
                                shutdownStarter();
                                close();
                            }
                            registrationParams.setHost(remote.getInetAddress().getHostAddress());
                            registrationParams.setPort(remote.getPort());
                            Response response = new Response();
                            response.setMsg(new VariableLengthString("注册成功"));
                            response.setStatus(Status.SUCCESS);
                            log.info("有新的模块成功注册={}", registrationParams);
                            moduleManager.addModule(registrationParams);
                            serverId = registrationParams.getServerId();
                            Response success = new Response();
                            success.setMsg(new VariableLengthString(String.valueOf(serverId)));
                            success.setStatus(Status.SUCCESS);
                            webLogRepeater.write(Command.REGISTER.getCode());
                            webLogRepeater.write(response.getPayload());
                        }
                    } catch (LogParserException | UnknownLogLevelException e) {
                        log.error("LogConnect.run() -日志解析异常- ", e);
                        webLogRepeater.error("日志解析异常，一些日志将被丢弃");
                        inputStream.skip(inputStream.available());
                    }
                }
            } catch (SocketException e) {
                log.error("LogConnect.run() -已经与 客户端 Socket 断开连接- ", e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                shutdownStarter();
            } catch (IOException e) {
                log.error("LogConnect.run() -关闭远端Socket失败- ", e);
            }
            moduleManager.removeModule(serverId);
        }
    }

    /**
     * 关闭远端发送器
     *
     * @throws IOException 输出关闭代码异常
     * @date 2024/3/22
     * @author YangGang
     */
    public void shutdownStarter() throws IOException {
        if (disconnecting) {
            remote.close();
        }
        disconnecting = true;
        webLogRepeater.write(Command.SHUTDOWN.getCode());
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