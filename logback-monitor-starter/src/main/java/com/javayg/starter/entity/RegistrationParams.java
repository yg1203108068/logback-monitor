package com.javayg.starter.entity;

import java.nio.ByteBuffer;

/**
 * 建立连接时，将本地服务注册到远程连接的参数
 *
 * @author YangGang
 * @date 2024/3/19
 */
public class RegistrationParams {
    /**
     * 协议版本
     */
    private final static byte protocolVersion = 2;

    /**
     * 服务名称
     */
    private final String serverName;

    public RegistrationParams(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 获取要发送的数据包
     *
     * @return 一个完整的数据包
     * @date 2024/3/4
     * @author YangGang
     * @description
     */
    public byte[] payload() {
        byte[] serverNameBytes = serverName.getBytes();
        int size = 1   // 1字节协议版本
                + serverNameBytes.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 + size); // 前面4字节放数据包负载的长度
        buffer.putInt(size)
                .put(protocolVersion)
                .put(serverNameBytes);
        return buffer.array();
    }

}
