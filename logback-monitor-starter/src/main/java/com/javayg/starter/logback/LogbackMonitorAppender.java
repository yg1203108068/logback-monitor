package com.javayg.starter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.javayg.starter.connect.LogbackSender;
import com.javayg.starter.entity.Log;
import com.javayg.starter.entity.MonitorProperties;
import com.javayg.starter.exception.ClientException;

/**
 * logback 日志 分析平台 推送 追加器
 *
 * @author YangGang
 * @date 2024/2/29
 * @description 负责向 日志分析平台 推送日志数据
 */
public class LogbackMonitorAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    MonitorProperties properties;

    private LogbackSender sender;

    /**
     * 构造 Logback 日志数据 分析平台追加器
     */
    public LogbackMonitorAppender(MonitorProperties properties) {
        setName("LogbackMonitorAppender");
        this.properties = properties;
    }


    @Override
    public void start() {
        this.sender = new LogbackSender(this);
        boolean start = sender.start(properties.getHost(), properties.getPort());
        if (!start) {
            addError("日志监控推送 Appender 启动失败");
            throw new ClientException("日志推送器无法启动");
        }
        addInfo("日志监控推送 Appender 已启动");
        super.start();
    }

    @Override
    public void stop() {
        if (sender != null) {
            sender.stop();
        }
        addInfo("日志监控推送 Appender 已终止");
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        Log logInfo = new Log(eventObject);
        sender.send(logInfo);
    }
}
