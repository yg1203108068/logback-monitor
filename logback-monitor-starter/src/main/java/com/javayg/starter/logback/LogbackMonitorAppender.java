package com.javayg.starter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.javayg.common.entity.CallChain;
import com.javayg.common.entity.Log;
import com.javayg.starter.connect.ClientContext;
import com.javayg.starter.connect.LogbackSender;
import com.javayg.starter.entity.MonitorProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * logback 日志 分析平台 推送 追加器
 *
 * @author YangGang
 * @date 2024/2/29
 * @description 负责向 日志分析平台 推送日志数据
 */
@Slf4j
public class LogbackMonitorAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    /**
     * 加载当前系统中对监视器的配置信息
     */
    MonitorProperties properties;
    /**
     * 本地服务的信息
     */
    ClientContext clientContext;

    /**
     * 定义一个发送器用来将日志发送到远端服务器
     */
    private LogbackSender sender;

    /**
     * 构造 Logback 日志数据 分析平台追加器
     *
     * @param properties    分析平台相关配置
     * @param clientContext 本地服务信息缓存
     */
    public LogbackMonitorAppender(MonitorProperties properties, ClientContext clientContext) {
        setName("LogbackMonitorAppender");
        this.properties = properties;
        this.clientContext = clientContext;
    }


    /**
     * 启动远端日志追加器的方法，通过调用启动方法，使远端追加器生效
     *
     * @date 2024/3/19
     * @author YangGang
     */
    @Override
    public void start() {
        this.sender = new LogbackSender(this, clientContext);
        sender.start(properties.getHost(), properties.getPort());
        super.start();
    }

    @Override
    public void stop() {
        if (sender != null) {
            sender.stop();
        }
        log.debug("日志监控推送 Appender 已终止");
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!isStarted()) {
            log.debug("远程日志追加器尚未启动");
        }
        CallChain callChain = clientContext.getCallChainInfo().get();
        Log logInfo = new Log(eventObject,callChain);
        sender.send(logInfo);
    }
}
