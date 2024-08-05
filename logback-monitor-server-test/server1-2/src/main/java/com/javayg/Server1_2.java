package com.javayg;

import lombok.extern.slf4j.Slf4j;
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
public class Server1_2 {
    public static void main(String[] args) throws InterruptedException {
        Server1_3.testLogPrint(log);
    }
}