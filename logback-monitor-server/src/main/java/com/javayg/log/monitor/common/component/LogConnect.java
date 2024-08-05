package com.javayg.log.monitor.common.component;

import cn.hutool.core.lang.Assert;
import com.javayg.common.constant.Command;
import com.javayg.common.constant.Status;
import com.javayg.common.entity.Log;
import com.javayg.common.entity.RegistrationParams;
import com.javayg.common.entity.Response;
import com.javayg.common.exception.UnknownLogLevelException;
import com.javayg.common.utils.ByteUtils;
import com.javayg.common.wrapper.VariableLengthString;
import com.javayg.log.monitor.common.exception.LogParserException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    // 已建立连接
    private boolean connected = true;
    // 连接被关闭
    private boolean disconnecting = false;
    // 日志中继器
    private final WebLogRepeater webLogRepeater;
    // 远程客户端连接
    private final Socket remote;
    // 客户端关闭回调
    private final ClientCloseCallBack clientCloseCallBack;
    // 日志解析工具
    private final LogParsing resolve;
    // 模块管理器
    private final ModuleManager moduleManager;
    // 当前客户端的服务id
    private int clientId;
    // 输出流
    private OutputStream outputStream;

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
                this.outputStream = remote.getOutputStream();
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
                            log.debug("接收到新的日志数据消息,clientId={}", clientId);
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
                            log.debug("调用链：{}", logInfo);
                            webLogRepeater.logHandler(logInfo, clientId);
                            log.debug("日志已处理");
                        } else if (command == Command.CLIENT_CLOSE.getCode()) {
                            webLogRepeater.warn("客户端已断开连接");
                            break;
                        } else if (command == Command.REGISTER.getCode()) {
                            RegistrationParams registrationParams = new RegistrationParams(inputStream);
                            byte protocolVersion = registrationParams.getProtocolVersion();
                            if (protocolVersion > 2) {
                                outputStream.write(Command.REGISTER.getCode());
                                Response response = new Response();
                                response.setMsg(new VariableLengthString("未知的版本"));
                                response.setStatus(Status.ERROR);
                                outputStream.write(response.getPayload());
                                shutdownStarter();
                                close();
                            }
                            registrationParams.setHost(remote.getInetAddress().getHostAddress());
                            registrationParams.setPort(remote.getPort());
                            clientId = registrationParams.getServerId();
                            Response response = new Response();
                            response.setMsg(new VariableLengthString(String.valueOf(clientId)));
                            response.setStatus(Status.SUCCESS);
                            log.info("有新的模块成功注册={}", registrationParams);
                            moduleManager.addModule(registrationParams);
                            outputStream.write(Command.REGISTER.getCode());
                            outputStream.write(response.getPayload());
                            log.info("颁发客户端id={}", clientId);
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
            moduleManager.removeModule(clientId);
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