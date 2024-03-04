package com.javayg.log.monitor.common.config.security.bo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 特殊地址访问权限控制配置
 *
 * @author 杨港
 * @date 2023/2/18
 * @description
 */
@Data
@Configuration
@ConfigurationProperties("url-access-limit")
public class UrlSecurityConfigProp {

    /**
     * 任意用户访问
     */
    private String[] permitAll;
    /**
     * 匿名用户访问
     */
    private String[] anonymous;
    /**
     * 通过认证即可访问
     */
    private String[] authenticated;
}
