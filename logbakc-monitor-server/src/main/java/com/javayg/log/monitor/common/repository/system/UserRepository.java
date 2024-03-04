package com.javayg.log.monitor.common.repository.system;

import com.javayg.log.monitor.common.entity.system.Token;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 杨港
 * @date 2023/2/12
 * @copyright
 * @description
 */
@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
    @SuppressWarnings("SpringDataRepositoryMethodParametersInspection")
    Optional<User> findByTokens(Token tokens);

    Optional<User> findByUsername(String username);

}
