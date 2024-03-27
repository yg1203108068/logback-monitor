package com.javayg.starter.interceptor;

import com.javayg.starter.connect.ClientContext;
import com.javayg.starter.constant.RpcHeaderNames;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.stereotype.Component;

/**
 * Feign 请求拦截器，发起请求前添加当前客户端id
 *
 * @author YangGang
 * @date 2024/3/27
 */
@Slf4j
public class MonitorFeignRequestInterceptor implements RequestInterceptor {
    ClientContext clientContext;

    public MonitorFeignRequestInterceptor(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void apply(RequestTemplate template) {
        log.info("添加请求头");
        template.header(RpcHeaderNames.SENDER_CLIENT_ID, clientContext.getClientId());
    }
}