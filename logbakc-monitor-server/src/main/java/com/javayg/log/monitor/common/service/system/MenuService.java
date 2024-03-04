package com.javayg.log.monitor.common.service.system;

import com.javayg.log.monitor.common.entity.system.Menu;
import com.javayg.log.monitor.common.entity.system.Role;
import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.exception.ApplicationException;
import com.javayg.log.monitor.common.repository.system.MenuRepository;
import com.javayg.log.monitor.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 权限服务
 *
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Service
public class MenuService extends BaseService<Menu, Long> {
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    UserService userService;

    public Collection<Menu> findByUsername(String username) {
        Optional<User> optUser = userService.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new ApplicationException("无法获取当前用户信息");
        }
        User user = optUser.get();
        Map<Integer, Menu> allMenu = new HashMap<>();
        for (Role role : user.getRoles()) {
            Set<Menu> menus = role.getMenus();
            for (Menu menu : menus) {
                allMenu.put(menu.getId(), menu);
            }
        }
        return allMenu.values();
    }
}
