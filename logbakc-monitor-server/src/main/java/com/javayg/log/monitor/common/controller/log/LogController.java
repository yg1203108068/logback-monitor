package com.javayg.log.monitor.common.controller.log;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.javayg.log.monitor.common.component.WebLogRepeater;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/log")
public class LogController {
    @Autowired
    private WebLogRepeater repeater;
    private final Snowflake generator = IdUtil.getSnowflake();

    @GetMapping(value = "/output", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> output(HttpServletRequest request) {
        System.out.println("日志显示端链接");
        long identity = generator.nextId();
        String clientKey = request.getRemoteHost() + "-" + identity;
        return Flux.<String>create(emitter -> repeater.addClient(clientKey, emitter))
                .doOnCancel(() -> {
                    log.info("{}已经关闭了链接", clientKey);
                    repeater.removeClient(null, clientKey);
                });
    }
}
