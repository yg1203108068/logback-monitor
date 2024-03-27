package com.javayg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
@RestController
@RequestMapping("/test")
@EnableFeignClients
public class Server3 {
    @Autowired
    Server4 server4;

    public static void main(String[] args) {
        SpringApplication.run(Server3.class);
    }

    @FeignClient(name = "server4", url = "localhost:8091/test")
    interface Server4 {
        @RequestMapping(method = RequestMethod.GET, value = "/")
        void test();
    }

    /**
     * 使用Feign调用其他服务
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/")
    public void test() {
        log.info("调用方 Server3.test() called");
        server4.test();
    }

    /**
     * 使用多线程处理
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/2")
    public void test2() {
        log.info("Server3.test2() called");
        server4.test();
    }

    /**
     * 在多线程中调用其他服务
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/3")
    public void test3() {
        log.info("Server3.test3() called");
        server4.test();
    }

    /**
     * 使用 Dubbo 调用其他服务
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/4")
    public void test4() {
        log.info("Server3.test4() called");
        server4.test();
    }

    /**
     * 使用 RMI 调用其他服务
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/5")
    public void test5() {
        log.info("Server3.test5() called");
        server4.test();
    }

    /**
     * 使用 RestTemplate 调用其他服务
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/6")
    public void test6() {
        log.info("Server3.test6() called");
        server4.test();
    }

    /**
     * 使用 WebClient 调用其他服务
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/7")
    public void test7() {
        log.info("Server3.test7() called");
        server4.test();
    }

    /**
     * 使用 HTTP 调用其他服务
     *
     * @date 2024/3/26
     * @author YangGang
     */
    @GetMapping("/8")
    public void test8() {
        log.info("Server3.test8() called");
        server4.test();
    }
}