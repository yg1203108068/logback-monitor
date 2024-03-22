package com.javayg.log.monitor.common.component;

import com.javayg.log.monitor.common.entity.net.RegistrationParams;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 模块管理器，管理所有通过 starter 注册来的模块
 *
 * @author YangGang
 * @date 2024/3/22
 */
@Component
public class ModuleManager {
    /**
     * 所有模块，以 serverId 为 key
     */
    private ConcurrentHashMap<Integer, RegistrationParams> modules = new ConcurrentHashMap<>(16);

    /**
     * 添加模块
     *
     * @param module 通过 starter 注册来的模块
     * @date 2024/3/22
     * @author YangGang
     */
    public void addModule(RegistrationParams module) {
        modules.put(module.getServerId(), module);
    }

    /**
     * 删除模块
     *
     * @param serverId 远程模块的服务id
     * @date 2024/3/22
     * @author YangGang
     */
    public void removeModule(int serverId) {
        modules.remove(serverId);
    }

    /**
     * 获取所有模块
     *
     * @date 2024/3/22
     * @author YangGang
     */
    public List<RegistrationParams> listModule() {
        return new ArrayList<>(modules.values());
    }

}
