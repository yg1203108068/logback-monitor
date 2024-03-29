package com.javayg.starter.connect;

import com.javayg.common.entity.CallChain;
import lombok.Getter;
import org.springframework.core.env.Environment;

/**
 * 用于缓存本地服务的信息
 *
 * @author YangGang
 * @date 2024/3/19
 */
@Getter
public class ClientContext {
    /**
     * 当前服务的名称
     */
    private final String serverName;
    /**
     * 远程监控端给颁发的服务id
     */
    private String clientId;
    /**
     * 用于保存调用链相关信息
     */
    private final InheritableThreadLocal<CallChain> callChainInfo = new InheritableThreadLocal<>();

    /**
     * 通过上下文环境信息构造本地服务信息缓存
     *
     * @param environment 当前系统的环境变量
     */
    public ClientContext(Environment environment) {
        this.serverName = environment.getProperty("spring.application.name");
    }

    protected void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
