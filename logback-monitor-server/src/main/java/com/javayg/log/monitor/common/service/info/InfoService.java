package com.javayg.log.monitor.common.service.info;

import cn.hutool.core.util.StrUtil;
import com.javayg.log.monitor.common.entity.Info;
import com.javayg.log.monitor.common.repository.info.InfoRepository;
import com.javayg.log.monitor.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 杨港
 * @date 2023/2/18
 * @description
 */
@Slf4j
@Service
public class InfoService extends BaseService<Info, Integer> {
    @Autowired
    InfoRepository infoRepository;

    public List<Info> findByCustomerNameLike(String customerName) {
        return infoRepository.findByCustomerNameLike("%" + customerName + "%");
    }

    public Page<Info> findPageList(String customerName, String projectName, Pageable pageable) {
        Info info = new Info();
        if (StrUtil.isNotEmpty(customerName)) {
            log.info("配置客户名称");
            info.setCustomerName(customerName);
        }
        if (StrUtil.isNotEmpty(projectName)) {
            log.info("配置项目名称");
            info.setProjectName(projectName);
        }
        ExampleMatcher matching = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("customerName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("projectName", ExampleMatcher.GenericPropertyMatchers.contains());
        return infoRepository.findAll(Example.of(info, matching), pageable);
    }

    public Page<Info> findMyPageList(String customerName, String projectName, String username, Pageable pageable) {
        Info info = new Info();
        if (StrUtil.isNotEmpty(customerName)) {
            log.info("配置客户名称");
            info.setCustomerName(customerName);
        }
        if (StrUtil.isNotEmpty(projectName)) {
            log.info("配置项目名称");
            info.setProjectName(projectName);
        }
        if (StrUtil.isNotEmpty(username)) {
            log.info("用户名");
            info.setCreateBy(username);
        }
        ExampleMatcher matching = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("customerName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("projectName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("createBy", ExampleMatcher.GenericPropertyMatcher::exact);
        return infoRepository.findAll(Example.of(info, matching), pageable);
    }
}
