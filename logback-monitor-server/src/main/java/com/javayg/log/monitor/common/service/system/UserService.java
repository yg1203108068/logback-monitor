package com.javayg.log.monitor.common.service.system;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.javayg.log.monitor.common.entity.system.Role;
import com.javayg.log.monitor.common.entity.system.Token;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.entity.vo.UserSaveVO;
import com.javayg.log.monitor.common.exception.ApplicationException;
import com.javayg.log.monitor.common.repository.system.UserRepository;
import com.javayg.log.monitor.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务
 *
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Slf4j
@Service
public class UserService extends BaseService<User, Integer> {
    @Autowired
    UserRepository userRepository;
    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${initPassword}")
    private String initPassword;
    @Autowired
    RoleService roleService;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByToken(String token) {
        return userRepository.findByTokens(new Token().setId(token));
    }

    public String generatePublicKey(User user) {
        RSA rsa = new RSA();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        user.setPublicKey(publicKeyBase64);
        user.setPrivateKey(rsa.getPrivateKeyBase64());
        save(user);
        return publicKeyBase64;
    }

    public Page<User> findPageList(String username, String nickname, Pageable pageable) {
        User user = new User();
        if (StrUtil.isNotEmpty(username)) {
            log.info("配置用户名");
            user.setUsername(username);
        }
        if (StrUtil.isNotEmpty(nickname)) {
            log.info("配置姓名");
            user.setNickname(nickname);
        }
        ExampleMatcher matching = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("valid")
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("nickname", ExampleMatcher.GenericPropertyMatchers.contains());
        return userRepository.findAll(Example.of(user, matching), pageable);
    }

    public User save(UserSaveVO formUser) {

        User user = new User();
        if (formUser.getId() != null) {
            Optional<User> one = findOne(formUser.getId());
            if (one.isEmpty()) {
                throw new ApplicationException("系统异常,用户不存在");
            }
            user = one.get();
        } else {
            user.setPassword(passwordEncoder.encode(initPassword));
        }
        user.setUsername(formUser.getUsername());
        user.setNickname(formUser.getNickname());
        List<Role> roles = roleService.findIds(formUser.getRoleId());
        user.setRoles(roles);
        log.info("保存信息：用户角色个数={}", user.getRoles().size());
        save(user);
        return user;
    }


    public void resetPassword(User user) {
        log.info("初始化密码：{}", initPassword);
        user.setPassword(passwordEncoder.encode(initPassword));
        user.setValid(true);
        save(user);
    }
}
