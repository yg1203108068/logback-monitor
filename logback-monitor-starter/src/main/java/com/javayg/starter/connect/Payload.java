package com.javayg.starter.connect;

import java.nio.ByteBuffer;

public abstract class Payload {
    public Payload(ByteBuffer buffer) {
        instantiate(buffer);
    }

    /**
     * 通过 ByteBuffer 创建对象实例
     *
     * @param payload 消息的负载
     * @date 2024/3/20
     * @author YangGang
     */
    abstract void instantiate(ByteBuffer payload);

    /**
     * 获取消息负载
     *
     * @return 消息负载
     * @date 2024/3/20
     * @author YangGang
     */
    abstract byte[] getPayload();
}
