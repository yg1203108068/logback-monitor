package com.javayg.starter.logback;

import com.javayg.starter.connect.ClientContext;
import com.javayg.starter.interceptor.MonitorFeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志监控与 Feign 相关配置
 *
 * @author YangGang
 * @date 2024/3/27
 */
@Slf4j
@Configuration
public class RpcAutoConfiguration {
    @Configuration
    @ConditionalOnClass(feign.RequestInterceptor.class)
    static class LogbackMonitorFeignAutoConfiguration {
        @Bean
        @ConditionalOnBean(ClientContext.class)
        public feign.RequestInterceptor logbackMonitorFeignRequestInterceptor(ClientContext clientContext) {
            log.info("LogbackMonitorFeignAutoConfiguration.monitorFeignRequestInterceptor() called");
            return new MonitorFeignRequestInterceptor(clientContext);
        }
    }
}