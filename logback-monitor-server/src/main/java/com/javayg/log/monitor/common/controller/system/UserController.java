package com.javayg.log.monitor.common.controller.system;

import com.javayg.log.monitor.common.entity.system.User;
import com.javayg.log.monitor.common.entity.vo.RestApi;
import com.javayg.log.monitor.common.entity.vo.UserListVO;
import com.javayg.log.monitor.common.entity.vo.UserSaveVO;
import com.javayg.log.monitor.common.exception.ApplicationException;
import com.javayg.log.monitor.common.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 杨港
 * @date 2023/2/17
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/list")
    public RestApi<Page<UserListVO>> list(@RequestParam String username, @RequestParam String nickname, @PageableDefault Pageable pageable) {
        log.info("获取列表");
        Page<User> pageList = userService.findPageList(username, nickname, pageable);
        List<UserListVO> users = pageList.stream().map(UserListVO::new).collect(Collectors.toList());
        Page<UserListVO> result = new PageImpl<>(users, pageList.getPageable(), pageList.getNumberOfElements());
        return RestApi.success(result);
    }

    @PostMapping("/add")
    public RestApi<String> add(@RequestBody UserSaveVO formUser) {
        log.info("保存用户{}", formUser);
        if (userService.findByUsername(formUser.getUsername()).isPresent()) {
            throw new ApplicationException("用户名已存在");
        }
        userService.save(formUser);
        return RestApi.success("提交成功");
    }

    @PostMapping("/edit")
    public RestApi<String> edit(@RequestBody UserSaveVO formUser) {
        log.info("修改用户{}", formUser);
        Optional<User> optionalUser = userService.findByUsername(formUser.getUsername());
        if (optionalUser.isEmpty()) {
            throw new ApplicationException("账户不存在");
        }
        userService.save(formUser);
        return RestApi.success("提交成功");
    }

    @GetMapping("/reset/{id}")
    public RestApi reset(@PathVariable Integer id) {
        log.info("重置密码：{}", id);
        Optional<User> optUser = userService.findOne(id);
        if (optUser.isEmpty()) {
            throw new ApplicationException("系统异常");
        }
        userService.resetPassword(optUser.get());
        return RestApi.success("密码已重置");
    }

    @GetMapping("/disable/{id}")
    public RestApi disable(@PathVariable Integer id) {
        log.info("禁用账户");
        Optional<User> optUser = userService.findOne(id);
        if (optUser.isEmpty()) {
            throw new ApplicationException("系统异常");
        }
        User user = optUser.get();
        user.setValid(!user.getValid());
        userService.save(user);
        return RestApi.success(user.getValid() ? "账户已启用" : "账户已禁用");
    }

    @GetMapping("/delete/{id}")
    public RestApi delete(@PathVariable Integer id) {
        log.info("删除账户");
        Optional<User> optUser = userService.findOne(id);
        if (optUser.isEmpty()) {
            throw new ApplicationException("系统异常");
        }
        User user = optUser.get();
        user.setDel(true);
        userService.save(user);
        return RestApi.success("账户已删除");
    }

}
