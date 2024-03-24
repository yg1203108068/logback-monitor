package com.javayg.starter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.javayg.starter.connect.LogbackSender;
import com.javayg.starter.entity.LocalServerCache;
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

    /**
     * 加载当前系统中对监视器的配置信息
     */
    MonitorProperties properties;
    /**
     * 本地服务的信息
     */
    LocalServerCache localServerCache;

    /**
     * 定义一个发送器用来将日志发送到远端服务器
     */
    private LogbackSender sender;

    /**
     * 构造 Logback 日志数据 分析平台追加器
     *
     * @param properties       分析平台相关配置
     * @param localServerCache 本地服务信息缓存
     */
    public LogbackMonitorAppender(MonitorProperties properties, LocalServerCache localServerCache) {
        setName("LogbackMonitorAppender");
        this.properties = properties;
        this.localServerCache = localServerCache;
    }


    /**
     * 启动远端日志追加器的方法，通过调用启动方法，使远端追加器生效
     *
     * @date 2024/3/19
     * @author YangGang
     */
    @Override
    public void start() {
        this.sender = new LogbackSender(this, localServerCache);
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
        if (!isStarted()) {
            addWarn("远程日志追加器尚未启动");
        }
        Log logInfo = new Log(eventObject);
        sender.send(logInfo);
    }
}
