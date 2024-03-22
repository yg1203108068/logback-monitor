package com.javayg.log.monitor.common.config.security.bo;

import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.entity.system.UserLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户的账户信息
 *
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Slf4j
public record UserAccountBO(User user) implements UserDetails {

    @Override
    public Collection<RoleAccountBO> getAuthorities() {
        List<RoleAccountBO> roles = new ArrayList<>();
        user.getRoles().forEach(role -> {
            roles.add(new RoleAccountBO(role));
        });
        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 帐户是否未过期
     *
     * @return true 未过期
     * <p>
     * 当前系统不涉及账户使用期限问题
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 帐号是否未锁定
     *
     * @return true 未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        List<UserLock> userLocks = user.getUserLock();
        if (user.getLocked() && (!CollectionUtils.isEmpty(userLocks)) && userLocks.size() != 1) {
            // 校验是否在锁定期间内,当前时间小于解锁时间则账户被锁定
            return userLocks.iterator().hasNext() && Instant.now().compareTo(userLocks.iterator().next().getUnlockTime()) < 0;
        }
        return true;
    }

    /**
     * 凭据是否未过期
     *
     * @return true 未过期
     * <p>
     * 当前系统不涉及凭证
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     *
     * @return 账号是有效的
     */
    @Override
    public boolean isEnabled() {
        return user.getValid();
    }
}
