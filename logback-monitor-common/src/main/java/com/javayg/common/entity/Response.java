package com.javayg.common.entity;

import com.javayg.common.constant.Status;
import com.javayg.common.exception.ResponseUnknownException;
import com.javayg.common.wrapper.VariableLengthString;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * 通用响应
 *
 * @author YangGang
 * @date 2024/3/22
 */
@Data
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Response(InputStream inputStream) throws IOException, ResponseUnknownException {
        status = Status.getInstance((byte) inputStream.read());
        msg = new VariableLengthString(inputStream);
    }

    public byte[] getPayload() {
        byte[] msgPayload = msg.getPayload();
        ByteBuffer buffer = ByteBuffer.allocate(1 + msgPayload.length);
        buffer.put(status.getCode());
        buffer.put(msgPayload);
        return buffer.array();
    }

}
