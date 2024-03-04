package com.javayg.log.monitor.common.config.webmvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@Slf4j
public class ViewInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        log.warn("{}访问了没有映射的地址: {} ,已被拦截，并返回首页", request.getRemoteHost(), request.getRequestURI());
        response.setStatus(200);
        request.getRequestDispatcher("/").forward(request, response);
        return false;
    }
}