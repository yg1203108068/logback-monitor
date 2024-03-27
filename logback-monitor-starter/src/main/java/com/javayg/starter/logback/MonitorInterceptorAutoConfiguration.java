package com.javayg.starter.logback;

import com.javayg.starter.connect.ClientContext;
import com.javayg.starter.interceptor.MonitorHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MonitorInterceptorAutoConfiguration {

    @Bean
    public ClientContext clientContext() {
        return RemoteLogbackMonitorConfigurator.getClientContext();
    }

    @Configuration
    public static class WebMvcConfig implements WebMvcConfigurer {

        @Autowired
        private MonitorHandlerInterceptor monitorHandlerInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(monitorHandlerInterceptor)
                    .addPathPatterns("/**");
        }
    }
}