package com.javayg.starter.interceptor;

import com.javayg.starter.connect.ClientContext;
import com.javayg.starter.constant.RpcHeaderNames;
import com.javayg.starter.entity.CallChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 用于记录调用链的拦截器
 *
 * @author YangGang
 * @date 2024/3/26
 */
public class MonitorHandlerInterceptor implements HandlerInterceptor {
    ClientContext clientContext;

    public MonitorHandlerInterceptor(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String prevClientId = request.getHeader(RpcHeaderNames.SENDER_CLIENT_ID);
        // 调用链ID
        String callChainId = UUID.randomUUID().toString();
        clientContext.getCallChainInfo().set(new CallChain(prevClientId, callChainId));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        clientContext.getCallChainInfo().remove();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
