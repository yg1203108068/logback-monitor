package com.javayg.starter.constant;

/**
 * 网络传输可用的命令
 *
 * @author YangGang
 * @date 2024/3/4
 */
public enum Command {
    /**
     * 端口连接
     */
    SHUTDOWN((byte) 0b1),   // 0000 0001
    /**
     * 传输日志
     */
    LOG((byte) 0b10),  // 0000 0010
    /**
     * 注册客户端
     */
    REGISTER((byte) 0b100),  // 0000 0100
    RESPONSE((byte) 0b1000),// 0000 1000
    ;
    private final byte code;

    public byte getCode() {
        return code;
    }

    Command(byte code) {
        this.code = code;
    }
}