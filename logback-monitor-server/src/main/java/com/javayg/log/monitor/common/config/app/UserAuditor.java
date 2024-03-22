package com.javayg.log.monitor.common.config.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@Slf4j
public class UserAuditor implements AuditorAware<String> {

    /**
     * 获取当前创建或修改的用户
     *
     * @return
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.ofNullable(user.getUsername());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}