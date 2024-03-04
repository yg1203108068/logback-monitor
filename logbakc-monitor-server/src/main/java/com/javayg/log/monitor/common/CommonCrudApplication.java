package com.javayg.log.monitor.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class CommonCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonCrudApplication.class, args);
    }
}
