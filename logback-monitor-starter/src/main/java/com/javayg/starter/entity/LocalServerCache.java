package com.javayg.starter.entity;

import lombok.Getter;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 用于缓存本地服务的信息
 * @author YangGang
 * @date 2024/3/19
 */
@Getter
public class LocalServerCache {
    /**
     * 当前服务的名称
     */
    private final String serverName;

    /**
     * 通过上下文环境信息构造本地服务信息缓存
     * @param environment 当前系统的环境变量
     */
    public LocalServerCache(ConfigurableEnvironment environment) {
        this.serverName = environment.getProperty("spring.application.name");
    }
}
