package com.javayg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 分布式架构中的其中一个服务
 *
 * @author YangGang
 * @date 2024/3/19
 * @description
 */
@Slf4j
@SpringBootApplication
public class Server1 {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Server1.class);
        List<Consumer<Integer>> list = new ArrayList<>();
        list.add(index -> log.trace("index={}, Server1.main() 调用", index));
        list.add(index -> log.debug("index={}, Server1.main() 调用", index));
        list.add(index -> log.info("index={}, Server1.main() 调用", index));
        list.add(index -> log.warn("index={}, Server1.main() 调用", index));
        list.add(index -> log.error("index={}, Server1.main() 调用", index));
        for (int i = 0; i < 10000; i++) {
            list.get(i % 5).accept(i);
            Thread.sleep(1000);
        }
    }
}