package com.javayg.log.monitor.common.config.app;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;

/**
 * @author 杨港
 * @date 2023/2/17
 * @description
 */
@Slf4j
@Configuration
public class ApplicationConfig {
    /**
     * 让懒加载可以在 View 层使用
     *
     * @return
     */
    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor(EntityManagerFactory entityManagerFactory) {
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactory);
        return openEntityManagerInViewInterceptor;
    }

}
