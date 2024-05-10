package com.javayg.common.entity;

import com.javayg.common.utils.IdentityGen;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 注册参数
 *
 * @author YangGang
 * @date 2024/3/22
 */
@Slf4j
@Data
public class RegistrationParams {
    /**
     * 服务id
     */
    private int serverId;
    /**
     * 协议版本
     */
    private byte protocolVersion;

    /**
     * 服务名称
     */
    private final String serverName;

    /**
     * 地址
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;

    public RegistrationParams(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 在 inputStream 中读取相关协议版本和服务名称
     *
     * @param inputStream 输入流
     * @throws IOException 读取失败
     */
    public RegistrationParams(InputStream inputStream) throws IOException {
        serverId = IdentityGen.nextIdentity();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int size = dataInputStream.readInt();

        byte[] protocolVersionBytes = new byte[1];
        inputStream.read(protocolVersionBytes);
        this.protocolVersion = protocolVersionBytes[0];

        byte[] serverNameBytes = new byte[size - 1];
        inputStream.read(serverNameBytes);
        this.serverName = new String(serverNameBytes, StandardCharsets.UTF_8);
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
