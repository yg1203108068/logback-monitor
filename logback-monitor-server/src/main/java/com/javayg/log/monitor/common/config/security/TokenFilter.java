package com.javayg.log.monitor.common.config.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.javayg.log.monitor.common.config.security.bo.UrlSecurityConfigProp;
import com.javayg.log.monitor.common.config.security.bo.UserAccountBO;
import com.javayg.log.monitor.common.entity.system.Token;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.service.system.TokenService;
import com.javayg.log.monitor.common.service.system.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import static com.javayg.log.monitor.common.constant.StringConstant.REQUEST_USER_ATTR;
import static com.javayg.log.monitor.common.constant.StringConstant.TOKEN_KEY;

@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    private UrlSecurityConfigProp urlSecurityConfigProp;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    private static final Map TOKEN_INVALID = Map.of("code", 401, "msg", "Token 无效");
    private static final Map TOKEN_MISSING = Map.of("code", 401, "msg", "缺少 Token");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = httpServletRequest.getRequestURI();
        log.info("Token 过滤器 对 {} 进行校验", requestURI);
        boolean access = false;
        for (String path : urlSecurityConfigProp.getPermitAll()) {
            if (pathMatcher.match(path, requestURI)) {
                access = true;
                break;
            }
        }
        String[] anonymous = urlSecurityConfigProp.getAnonymous();
        if (!access) {
            for (String path : anonymous) {
                if (pathMatcher.match(path, requestURI)) {
                    access = true;
                    break;
                }
            }
        }

        if (access) {
            log.info("访问了直接放行的接口,不进行Token校验");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String tokenStr = getToken(httpServletRequest);
        if (StrUtil.isEmpty(tokenStr)) {
            // 缺少 Token
            log.warn("缺少 Token");
            outPrint(httpServletResponse, TOKEN_MISSING);
            return;
        }
        log.info("tokenStr={}", tokenStr);
        Optional<Token> optToken = tokenService.findOne(tokenStr);
        if (optToken.isEmpty()) {
            // Token 无效
            log.warn("Token 无效");
            outPrint(httpServletResponse, TOKEN_INVALID);
            return;
        }
        Token token = optToken.get();
        log.info("Token验证,当结果大于0时 Token 过期 , 验证结果 = {}", Instant.now().compareTo(token.getExpiration()) > 0 ? "Token 过期" : "验证通过");
        if (Instant.now().compareTo(token.getExpiration()) > 0) {
            // Token 过期
            outPrint(httpServletResponse, TOKEN_INVALID);
            return;
        }
        if (httpServletRequest.getHeader("DEBUG") == null) {
            // DEBUG 模式不对Token进行调整
            token.setExpiration(Instant.now().plus(30, ChronoUnit.MINUTES));
            tokenService.save(token);
        }

        Optional<User> user = userService.findByToken(tokenStr);
        UserAccountBO userAccountBO = new UserAccountBO(user.get());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccountBO, tokenStr, userAccountBO.getAuthorities());
        httpServletRequest.setAttribute(REQUEST_USER_ATTR, user.get());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public void outPrint(HttpServletResponse response, Map result) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSONUtil.toJsonPrettyStr(result));
    }

    public String getToken(HttpServletRequest httpServletRequest) {
        String tokenStr = httpServletRequest.getHeader(TOKEN_KEY);
        if (StrUtil.isNotBlank(tokenStr)) {
            return tokenStr;
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equalsIgnoreCase(TOKEN_KEY)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}