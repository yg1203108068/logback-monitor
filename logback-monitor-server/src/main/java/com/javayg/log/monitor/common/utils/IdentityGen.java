package com.javayg.log.monitor.common.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 全局id生成器
 *
 * @author YangGang
 * @date 2024/3/22
 */
public class IdentityGen {
    private static AtomicInteger id = new AtomicInteger(0);

    /**
     * 获取下一个id
     *
     * @return
     * @date 2024/3/22
     * @author YangGang
     */
    public static int nextIdentity() {
        return id.getAndIncrement();
    }
}
