package com.javayg.log.monitor.common.controller.system;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.javayg.log.monitor.common.entity.system.Menu;
import com.javayg.log.monitor.common.entity.system.Token;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.entity.vo.RestApi;
import com.javayg.log.monitor.common.entity.vo.UserChangePasswordVO;
import com.javayg.log.monitor.common.entity.vo.UserVO;
import com.javayg.log.monitor.common.exception.ApplicationException;
import com.javayg.log.monitor.common.service.system.MenuService;
import com.javayg.log.monitor.common.service.system.TokenService;
import com.javayg.log.monitor.common.service.system.UserService;
import com.javayg.log.monitor.common.utils.OptionalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class IndexController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;
    @Autowired
    TokenService tokenService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public RestApi<UserVO> login(@RequestBody UserVO userForm) {
        log.info("用户 {} 正在登录", userForm.getUsername());
        Optional<User> loginUser = userService.findByUsername(userForm.getUsername());
        if (loginUser.isEmpty()) {
            throw new ApplicationException("用户名不存在");
        }
        log.info("userForm.getPassword()={}", userForm.getPassword());
        User user = loginUser.get();
        RSA rsa = new RSA(user.getPrivateKey(), null);
        byte[] decrypt = rsa.decrypt(Base64.decode(userForm.getPassword().getBytes()), KeyType.PrivateKey);
        // 认证用户
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userForm.getUsername(), new String(decrypt)));
        // 设置Token
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(tokenService.createToken(user));
        user.setTokens(tokens);
        // 解除锁定(设置锁定状态为false)
        user.setLocked(false);
        user.setLastLoginTime(Instant.now());
        userService.save(user);
        return RestApi.success(new UserVO(user));
    }

    @GetMapping("/menu")
    public RestApi<Collection<Menu>> getMenu(Principal principal) {
        String username = principal.getName();
        log.info("当前登陆人" + principal.getName());
        return RestApi.success(menuService.findByUsername(username));
    }

    @GetMapping("/nickname")
    public RestApi<String> getNickname(Principal principal) {
        String username = principal.getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            throw new ApplicationException("用户不存在");
        }
        return RestApi.success(user.get().getNickname());
    }

    @GetMapping("/username/{username}")
    public RestApi<String> loginUsername(@PathVariable String username) {
        log.info("tokenService==null = {}", tokenService);
        Optional<User> optUser = userService.findByUsername(username);
        OptionalUtils.checkEmpty(optUser, "用户不存在");
        return RestApi.success(userService.generatePublicKey(optUser.get()));
    }

    @GetMapping("/token")
    public RestApi<String> getToken(Principal principal) {
        Optional<User> optUser = userService.findByUsername(principal.getName());
        OptionalUtils.checkEmpty(optUser, "用户不存在");
        log.info("optUser.isEmpty=>{}", optUser.isEmpty());
        return RestApi.success(userService.generatePublicKey(optUser.get()));
    }

    @PostMapping("/password")
    public RestApi<String> changePassword(@RequestBody UserChangePasswordVO userChangePasswordVO, Principal principal) {
        Optional<User> optUser = userService.findByUsername(principal.getName());
        OptionalUtils.checkEmpty(optUser, "用户不存在");
        User user = optUser.get();
        log.info("用户：{} 正在改密码", user.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principal.getName(), userChangePasswordVO.getOldPassword(user.getPrivateKey())));
        user.setPassword(passwordEncoder.encode(userChangePasswordVO.getNewPassword(user.getPrivateKey())));
        userService.save(user);
        return RestApi.success("修改成功");
    }

}
