package com.javayg.log.monitor.common.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 杨港
 * @date 2023/2/25
 * @description
 */
@Data
public class UserSaveVO {

    private Integer id;
    private String username;
    private String nickname;
    private List<Integer> roleId;
}
