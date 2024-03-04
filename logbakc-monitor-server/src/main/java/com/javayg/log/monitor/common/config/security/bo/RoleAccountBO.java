package com.javayg.log.monitor.common.config.security.bo;

import com.javayg.log.monitor.common.entity.system.Role;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用户的账户信息
 *
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
public class RoleAccountBO implements GrantedAuthority {
    private final Role role;
    private final String roleStr;

    public RoleAccountBO(Role role) {
        this.role = role;
        this.roleStr = role.getCode();
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + roleStr;
    }
}
