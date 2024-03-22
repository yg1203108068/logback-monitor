package com.javayg.log.monitor.common.config.security;

import cn.hutool.json.JSONUtil;
import com.javayg.log.monitor.common.config.security.bo.UserAccountBO;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.entity.vo.RestApi;
import com.javayg.log.monitor.common.exception.ApplicationException;
import com.javayg.log.monitor.common.service.system.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * 安全认证配置类
 *
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * 指定用户密码加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserService userService;

    /**
     * 配置安全认证执行链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RoleConfig roleConfig, TokenFilter tokenFilter) throws Exception {
        http
                // 禁用跨站保护
                .csrf().disable()
                // 前后端分离，不用创建HttpSession
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 配置角色和权限
                .authorizeHttpRequests(roleConfig)
                // 禁用登录表单
                .formLogin().disable()
                // 异常处理器，用于认证失败时，给前端提示信息
                .exceptionHandling(e -> {
                    e.authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().println(JSONUtil.toJsonPrettyStr(RestApi.fail(authException.getMessage(), HttpStatus.UNAUTHORIZED.value())));
                    });
                    e.accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().println(JSONUtil.toJsonPrettyStr(RestApi.fail(accessDeniedException.getMessage(), HttpStatus.UNAUTHORIZED.value())));
                    });
                })
                // 处理退出
                .logout().logoutUrl("/api/logout")
                // 退出成功处理
                .logoutSuccessHandler(this::logoutSuccessHandler)
                .and()
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    /**
     * 获取登陆人信息
     */
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            Optional<User> user = userService.findByUsername(username);
            if (user.isEmpty()) {
                log.info("用户不存在");
                throw new ApplicationException("用户不存在");
            }
            return new UserAccountBO(user.get());
        };
    }


    /**
     * 手动调用，自动认证
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }


    /**
     * 退出成功处理器
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    public void logoutSuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = request.getHeader("token");
        log.info("{} 退出系统", token);
        Optional<User> loginUser = userService.findByToken(token);
        if (loginUser.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(JSONUtil.toJsonPrettyStr(Map.of("code", 200, "data", "", "msg", "已退出系统")));
            return;
        }
        User user = loginUser.get();
        user.setTokens(null);
        userService.save(user);
    }
}
