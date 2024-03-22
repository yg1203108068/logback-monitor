package com.javayg.starter.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.javayg.starter.entity.LocalServerCache;
import com.javayg.starter.entity.MonitorProperties;
import com.javayg.starter.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * LoggerConfigurator 的 SPI 配置
 *
 * @author YangGang
 * @date 2024/3/3
 * @description 在应用程序初始化时，创建追加器并启动
 */
public class RemoteLogbackMonitorConfigurator implements GenericApplicationListener {
    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        if (eventType.getRawClass() != null) {
            return eventType.getRawClass().isAssignableFrom(ApplicationContextInitializedEvent.class);
        }
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 获取配置信息
        ConfigurableApplicationContext applicationContext = ((ApplicationContextInitializedEvent)event).getApplicationContext();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        // 获取当前日志系统的上下文
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        Logger log = loggerContext.getLogger("ROOT");

        MonitorProperties properties = new MonitorProperties(environment);
        LocalServerCache localServerInfo = new LocalServerCache(environment);
        LogbackMonitorAppender logbackMonitorAppender = new LogbackMonitorAppender(properties,localServerInfo);
        logbackMonitorAppender.setContext(loggerContext);
        try {
            logbackMonitorAppender.start();
        } catch (ClientException e) {
            log.warn("远程日志分析平台数据追加器 启动失败，详细信息可修改 logback.xml 的 <configuration> 标签，添加 debug=\"true\" 属性查看。");
            return;
        }

        // 将 appender 添加到 logger 的上下文中，开始收集日志
        log.addAppender(logbackMonitorAppender);
        log.info("添加 Appender 远程日志分析平台地址：" + properties.getHost() + ":" + properties.getPort());
    }
}