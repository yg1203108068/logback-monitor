package com.javayg.starter.entity;

import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 配置
 *
 * @author YangGang
 * @date 2024/3/1
 * @description
 */
public class MonitorProperties {
    /**
     * 接收方地址
     */
    private String host;
    /**
     * 接收方端口
     */
    private Integer port;

    public MonitorProperties(ConfigurableEnvironment environment) {
        this.host = environment.getProperty("logback.monitor.host");
        this.port = environment.getProperty("logback.monitor.port", Integer.class);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}