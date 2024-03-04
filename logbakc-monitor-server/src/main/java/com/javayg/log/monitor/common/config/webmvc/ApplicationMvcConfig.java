package com.javayg.log.monitor.common.config.webmvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 杨港
 * @date 2023/2/13
 * @description
 */
@Slf4j
@Configuration
public class ApplicationMvcConfig implements WebMvcConfigurer {
    @Autowired
    ViewInterceptor viewInterceptor;
    @Autowired
    OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor;

    /**
     * 配置异常处理器
     *
     * @return
     */
    @Bean
    public ExceptionHandlingConfigurer exceptionHandlingConfigurer() {
        return new ExceptionHandlingConfigurer();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // web客户端View跳转拦截器
        registry.addInterceptor(viewInterceptor).excludePathPatterns("/", "/*.html", "/api/**", "/log/**", "/root/**", "/static/**/**", "/favicon.ico");
        // jpa-in-view 拦截处理器
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor).addPathPatterns("/**");
    }

}
