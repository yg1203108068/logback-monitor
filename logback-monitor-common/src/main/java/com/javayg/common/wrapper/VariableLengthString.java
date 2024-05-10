package com.javayg.common.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 可变长度的字符串
 * 类型结构为 4字节字符串字节长度 + 字符串负载
 *
 * @author YangGang
 * @date 2024/3/22
 */
public class VariableLengthString {
    /**
     * 字符串所在字节长度(4字节)
     */
    FixedLengthNumber length;
    /**
     * 字符串负载
     */
    private byte[] contentBytes;

    /**
     * 构造一个带长度的字符串对象
     *
     * @param content 字符粗
     */
    public VariableLengthString(String content) {
        if (content != null) {
            this.contentBytes = content.getBytes(StandardCharsets.UTF_8);
            this.length = new FixedLengthNumber(contentBytes.length);
        } else {
            this.length = new FixedLengthNumber(0);
        }
    }

    public VariableLengthString(InputStream inputStream) throws IOException {
        length = new FixedLengthNumber(inputStream, FixedLengthNumber.NumberLength.FORE);
        contentBytes = new byte[length.intValue()];
        inputStream.read(contentBytes);
    }

    /**
     * 通过缓冲器获取
     *
     * @param buffer
     */
    public VariableLengthString(ByteBuffer buffer) {
        length = new FixedLengthNumber(buffer, FixedLengthNumber.NumberLength.FORE);
        contentBytes = new byte[length.intValue()];
        buffer.get(contentBytes);
    }

    public VariableLengthString() {
        this.length = new FixedLengthNumber(0);
    }

    /**
     * 获取负载
     *
     * @return 实际负载
     * @date 2024/3/22
     * @author YangGang
     */
    public byte[] getContentBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(length.getLength() + (contentBytes != null ? contentBytes.length : 0));
        buffer.put(length.payload);
        if (contentBytes != null) {
            buffer.put(contentBytes);
        }
        return buffer.array();
    }

    /**
     * 获取内容
     *
     * @return 获取字符串内容
     * @date 2024/3/22
     * @author YangGang
     */
    public String getContent() {
        return new String(contentBytes, StandardCharsets.UTF_8);
    }

    /**
     * 获取负载
     *
     * @return 实际负载
     * @date 2024/3/22
     * @author YangGang
     */
    public byte[] getPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(length.getLength() + contentBytes.length);
        buffer.put(length.payload);
        if (contentBytes != null) {
            buffer.put(contentBytes);
        }
        return buffer.array();
    }
}
