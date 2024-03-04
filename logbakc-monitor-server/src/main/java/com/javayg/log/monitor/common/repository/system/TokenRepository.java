package com.javayg.log.monitor.common.repository.system;

import com.javayg.log.monitor.common.entity.system.Token;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 杨港
 * @date 2023/2/12
 * @copyright
 * @description
 */
@Repository
public interface TokenRepository extends BaseRepository<Token, String> {
    /**
     * 通过用户清空token
     *
     * @param user
     * @return
     * @date 2023/2/24
     * @author YangGang
     * @description
     */
    @Modifying
    @Transactional
    long deleteByUser(User user);

}
