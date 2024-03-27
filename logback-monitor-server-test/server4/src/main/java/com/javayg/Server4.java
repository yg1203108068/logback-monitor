package com.javayg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@SpringBootApplication
public class Server4 {
    public static void main(String[] args) {
        SpringApplication.run(Server4.class);
    }

    @GetMapping("/")
    public void test() {
        log.info("被调用 Server4.test() called");
        log.info("被调用 Server4.test() called2");
    }
}