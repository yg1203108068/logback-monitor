package com.javayg.log.monitor.common.component;

/**
 * 客户端链接关闭回调
 *
 * @author YangGang
 * @date 2024/2/26
 * @description
 */
@FunctionalInterface
public interface ClientCloseCallBack {
    void close(LogConnect client);
}
