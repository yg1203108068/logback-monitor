package com.javayg.starter.entity;

import com.javayg.starter.exception.ResponseUnknownException;
import com.javayg.starter.wrapper.VariableLengthString;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通用响应
 *
 * @author YangGang
 * @date 2024/3/22
 */
@Getter
@Setter
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
        byte[] statusCode = new byte[1];
        inputStream.read(statusCode);
        status = Status.getInstance(statusCode[0]);
        msg = new VariableLengthString(inputStream);
    }
}
