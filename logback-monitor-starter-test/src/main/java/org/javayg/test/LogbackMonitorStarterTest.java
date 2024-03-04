package org.javayg.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogbackMonitorStarterTest {
    private static final Logger log = LoggerFactory.getLogger(LogbackMonitorStarterTest.class);

    public static void main(String[] args) {
        SpringApplication.run(LogbackMonitorStarterTest.class, args);

        // 日志输出测试
        new Thread() {
            @Override
            public void run() {
                print();
            }

            private void print() {
                log.info("hello word!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                print();
            }
        }.start();
    }

}