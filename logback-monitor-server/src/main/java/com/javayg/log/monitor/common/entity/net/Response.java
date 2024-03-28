package com.javayg.log.monitor.common.entity.net;

import com.javayg.log.monitor.common.entity.net.wapper.VariableLengthString;
import com.javayg.log.monitor.common.exception.ResponseUnknownException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * 通用响应
 *
 * @author YangGang
 * @date 2024/3/22
 */
@Getter
@Setter
@NoArgsConstructor
public class Response {
    /**
     * 响应状态
     */
    private Status status;
    /**
     * 消息
     */
    private VariableLengthString msg;

    public byte[] getPayload() {
        byte[] msgPayload = msg.getPayload();
        ByteBuffer buffer = ByteBuffer.allocate(1 + msgPayload.length);
        buffer.put(status.getCode());
        buffer.put(msgPayload);
        return buffer.array();
    }


}
