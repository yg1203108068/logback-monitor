package com.javayg.log.monitor.common.entity.vo;

import com.javayg.log.monitor.common.entity.system.Role;
import com.javayg.log.monitor.common.entity.system.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 杨港
 * @date 2023/2/25
 * @description
 */
@Data
public class UserListVO {

    private Integer id;
    private String username;
    private String nickname;
    private String roleName;
    private Boolean valid;
    private List<Integer> roleId;

    public UserListVO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.roleName = user.getRoles().parallelStream().map(Role::getRoleName).collect(Collectors.joining());
        this.roleId = user.getRoles().parallelStream().map(Role::getId).collect(Collectors.toList());
        this.valid = user.getValid();
    }
}
