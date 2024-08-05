package com.javayg.log.monitor.common.component;

import com.javayg.common.entity.RegistrationParams;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模块管理器，管理所有通过 starter 注册来的模块
 *
 * @author YangGang
 * @date 2024/3/22
 */
@Component
public class ModuleManager {
    /**
     * 所有当前在线的模块id，用于在心跳包中推送给客户端
     */
    private final List<Integer> moduleIds = new LinkedList<>();
    /**
     * 所有模块，以 serverId 为 key
     */
    private final ConcurrentHashMap<Integer, RegistrationParams> modules = new ConcurrentHashMap<>(16);

    /**
     * 添加模块
     *
     * @param module 通过 starter 注册来的模块
     * @date 2024/3/22
     * @author YangGang
     */
    public void addModule(RegistrationParams module) {
        moduleIds.add(module.getServerId());
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
        moduleIds.remove(serverId);
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

    /**
     * 获取当前所有模块中的服务id
     *
     * @return 模块id数组
     * @author YangGang
     * Created on 2024/7/21
     */
    public Integer[] arrayModuleIds() {
        return moduleIds.toArray(new Integer[0]);
    }
}
