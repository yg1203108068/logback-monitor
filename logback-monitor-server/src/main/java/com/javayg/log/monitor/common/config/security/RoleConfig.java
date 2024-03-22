package com.javayg.log.monitor.common.config.security;

import cn.hutool.core.collection.CollectionUtil;
import com.javayg.log.monitor.common.config.security.bo.UrlSecurityConfigProp;
import com.javayg.log.monitor.common.entity.system.Menu;
import com.javayg.log.monitor.common.entity.system.Role;
import com.javayg.log.monitor.common.service.system.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * URL 角色的配置
 *
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Slf4j
@Component
public class RoleConfig implements Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {

    @Autowired
    MenuService menuService;
    @Autowired
    private UrlSecurityConfigProp urlSecurityConfigProp;

    @Override
    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {

        // 允许匿名用户访问
        authorizationManagerRequestMatcherRegistry.requestMatchers(urlSecurityConfigProp.getAnonymous()).anonymous();
        // 错误接口，允许任何人访问
        authorizationManagerRequestMatcherRegistry.requestMatchers(urlSecurityConfigProp.getPermitAll()).permitAll();
        // 通过认证的，都可以访问
        authorizationManagerRequestMatcherRegistry.requestMatchers(urlSecurityConfigProp.getAuthenticated()).authenticated();
        // API接口，需要对应角色才能访问
        List<Menu> allMenu = menuService.findAll();
        for (Menu menu : allMenu) {
            List<Role> roles = menu.getRoles();
            if (CollectionUtil.isEmpty(roles)) {
                continue;
            }
            // 指定需要认证的路径
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl = authorizationManagerRequestMatcherRegistry.requestMatchers(menu.getPath());

            String[] roleList = new String[roles.size()];
            for (int i = 0; i < roles.size(); i++) {
                roleList[i] = roles.get(i).getCode();
            }
            authorizedUrl.hasAnyRole(roleList);
        }
    }
}
