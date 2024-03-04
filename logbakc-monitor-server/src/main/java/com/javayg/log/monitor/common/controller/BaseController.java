package com.javayg.log.monitor.common.controller;

import com.javayg.log.monitor.common.constant.StringConstant;
import com.javayg.log.monitor.common.entity.system.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 杨港
 * @date 2023/2/24
 * @description
 */
public class BaseController {
    @Autowired(required = false)
    HttpServletRequest request;

    public User getUser() {
        return (User)request.getAttribute(StringConstant.REQUEST_USER_ATTR);
    }
}
