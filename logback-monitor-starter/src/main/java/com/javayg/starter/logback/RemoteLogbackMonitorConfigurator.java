package com.javayg.starter.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.javayg.starter.connect.ClientContext;
import com.javayg.starter.entity.MonitorProperties;
import com.javayg.starter.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
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
    private static ClientContext clientContext;
    private static LogbackMonitorAppender logbackMonitorAppender;

    protected static ClientContext getClientContext() {
        return clientContext;
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        if (eventType.getRawClass() != null) {
            return eventType.getRawClass().isAssignableFrom(ApplicationContextInitializedEvent.class)
                    || eventType.getRawClass().isAssignableFrom(ContextClosedEvent.class);
        }
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationContextInitializedEvent) {
            init((ApplicationContextInitializedEvent) event);
        } else if (event instanceof ContextClosedEvent) {
            close();
        }
    }

    /**
     * 初始化远程日志追加器
     *
     * @param event 启动事件
     * @date 2024/3/28
     * @author YangGang
     */
    private static void init(ApplicationContextInitializedEvent event) {
        // 获取配置信息
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        // 获取当前日志系统的上下文
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger log = loggerContext.getLogger("ROOT");

        MonitorProperties prop = new MonitorProperties(environment);
        clientContext = new ClientContext(environment);
        logbackMonitorAppender = new LogbackMonitorAppender(prop, clientContext);
        logbackMonitorAppender.setContext(loggerContext);
        try {
            logbackMonitorAppender.start();
        } catch (ClientException e) {
            log.warn("远程日志分析平台数据追加器 启动失败，详细信息可修改 logback.xml 的 <configuration> 标签，添加 debug=\"true\" 属性查看。");
            return;
        }

        // 将 appender 添加到 logger 的上下文中，开始收集日志
        log.addAppender(logbackMonitorAppender);
        log.info("添加 Appender 远程日志分析平台地址：" + prop.getHost() + ":" + prop.getPort());
    }

    /**
     * 程序关闭
     *
     * @date 2024/3/28
     * @author YangGang
     */
    private void close() {
        System.out.println("关闭回调");
        logbackMonitorAppender.stop();
    }
}