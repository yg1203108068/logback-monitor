package com.javayg.log.monitor.common.constant;

import lombok.Getter;

/**
 * 网络传输可用的命令
 *
 * @author YangGang
 * @date 2024/3/4
 */
public enum Command {
    CLIENT_CLOSE((byte)-1),
    /**
     * 端口连接
     */
    SHUTDOWN((byte)0b1),   // 0000 0001
    /**
     * 传输日志
     */
    LOG((byte)0b10);  // 0000 0010
    @Getter
    private final byte code;

    Command(byte code) {
        this.code = code;
    }
}