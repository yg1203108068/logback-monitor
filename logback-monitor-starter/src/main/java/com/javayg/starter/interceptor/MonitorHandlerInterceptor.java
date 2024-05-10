package com.javayg.starter.interceptor;

import com.javayg.common.entity.CallChain;
import com.javayg.starter.connect.ClientContext;
import com.javayg.starter.constant.RpcHeaderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
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
@Component
@ConditionalOnBean(ClientContext.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MonitorHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    ClientContext clientContext;

    public MonitorHandlerInterceptor(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String prevClientId = request.getHeader(RpcHeaderNames.SENDER_CLIENT_ID);
        String mainCallChainId = request.getHeader(RpcHeaderNames.MAIN_CALL_CHAIN_ID);
        String pathInfo = request.getServletPath();
        // 调用链ID
        String callChainId = UUID.randomUUID().toString();
        if (mainCallChainId == null || mainCallChainId.trim().isEmpty()) {
            clientContext.getCallChainInfo().set(new CallChain(prevClientId, callChainId, callChainId, pathInfo));
        } else {
            clientContext.getCallChainInfo().set(new CallChain(prevClientId, callChainId, mainCallChainId, pathInfo));
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        clientContext.getCallChainInfo().remove();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
