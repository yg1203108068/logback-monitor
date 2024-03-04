package com.javayg.log.monitor.common.entity.vo;

import com.javayg.log.monitor.common.entity.system.User;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于响应用户登陆成功后返回token,和获取登录接口的用户名密码
 *
 * @author YangGang
 * @date 2023/2/20
 * @description
 */
@Data
@NoArgsConstructor
public class UserVO {
    public UserVO(User user) {
        this.token = user.getTokens().get(0).getId();
    }

    private String username;
    private String password;
    private String token;
}