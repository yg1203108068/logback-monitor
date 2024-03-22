package com.javayg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分布式架构中的其中一个服务
 *
 * @author YangGang
 * @date 2024/3/19
 * @description
 */
@Slf4j
@SpringBootApplication
public class Server2 {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Server2.class);
        for (int i = 0; i < 10000; i++) {
            log.info("Server2.main() 调用");
            Thread.sleep(1000);
        }
    }
}