package com.javayg.starter.entity;

import lombok.Getter;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 配置
 *
 * @author YangGang
 * @date 2024/3/1
 * @description
 */
@Getter
public class MonitorProperties {
    /**
     * 接收方地址
     */
    private final String host;
    /**
     * 接收方端口
     */
    private final Integer port;

    public MonitorProperties(ConfigurableEnvironment environment) {
        this.host = environment.getProperty("logback.monitor.host");
        this.port = environment.getProperty("logback.monitor.port", Integer.class);
    }

}