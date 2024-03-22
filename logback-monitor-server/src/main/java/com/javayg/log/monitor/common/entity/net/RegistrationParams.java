package com.javayg.log.monitor.common.entity.net;

import com.javayg.log.monitor.common.utils.IdentityGen;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final byte protocolVersion;

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
}
