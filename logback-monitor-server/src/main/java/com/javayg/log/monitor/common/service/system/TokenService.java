package com.javayg.log.monitor.common.service.system;

import com.javayg.log.monitor.common.entity.system.Token;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.repository.system.TokenRepository;
import com.javayg.log.monitor.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Slf4j
@Service
public class TokenService extends BaseService<Token, String> {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserService userService;

    public Token createToken(User user) {
        // 清空当前用户的 Token
        tokenRepository.deleteByUser(user);
        // 创建新 Token
        Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setUser(user);
        token.setCreateTime(new Date());
        token.setExpiration(Instant.now().plus(30, ChronoUnit.MINUTES));
        return save(token);
    }

}
