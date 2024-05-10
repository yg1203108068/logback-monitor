package com.javayg.common.wrapper;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * 定长字符串
 *
 * @author YangGang
 * @date 2024/3/22
 */
public class FixedLengthNumber {
    /**
     * 保存数值的负载
     */
    public byte[] payload;
    @Getter
    private final int length;

    /**
     * 通过给定的数字所占用的字节，在输入流中获取数值
     *
     * @param inputStream  输入流
     * @param numberLength 数字的长度
     */
    public FixedLengthNumber(InputStream inputStream, NumberLength numberLength) throws IOException {
        length = numberLength.len;
        payload = new byte[numberLength.len];
        inputStream.read(payload);
    }

    public FixedLengthNumber(ByteBuffer buffer, NumberLength numberLength) {
        length = numberLength.len;
        payload = new byte[length];
        buffer.get(payload);
    }

    /**
     * 将当前FixedLengthNumber对象中的payload字节数组转换为一个int类型的数值。
     * 假设payload字节数组按照大端字节序（Big-Endian）存储了一个int类型的值。
     *
     * @return 转换后的int数值, 小端4字节
     * @throws BufferUnderflowException 如果payload字节数组的长度小于4个字节，将抛出此异常
     * @date 2024/3/28
     * @author YangGang
     */
    public int intValue() {
        return ByteBuffer.wrap(payload).getInt();
    }

    /**
     * 将当前FixedLengthNumber对象中的payload字节数组转换为一个long类型的数值。
     * 假设payload字节数组按照大端字节序（Big-Endian）存储了一个long类型的值。
     *
     * @return 转换后的long数值, 小端8字节
     * @throws BufferUnderflowException 如果payload字节数组的长度小于8个字节，将抛出此异常
     * @date 2024/3/28
     * @author YangGang
     */
    public long longValue() {
        return ByteBuffer.wrap(payload).getLong();
    }

    /**
     * 构造一个固定长度的数值类型
     *
     * @param value 数值
     * @param len   数值的长度
     */
    public FixedLengthNumber(Number value, NumberLength len) {
        this.length = len.len;
        payload = new byte[length];
        switch (len) {
            case ONE:
                payload[0] = (byte) ((int) value);
                break;
            case FORE:
                int number = (int) value;
                payload[0] = (byte) (number >> 24);
                payload[1] = (byte) (number >> 16);
                payload[2] = (byte) (number >> 8);
                payload[3] = (byte) number;
                break;
            case EIGHT:
                long longNumber = (long) value;
                payload[0] = (byte) (longNumber >> 56);
                payload[1] = (byte) (longNumber >> 48);
                payload[2] = (byte) (longNumber >> 40);
                payload[3] = (byte) (longNumber >> 32);
                payload[4] = (byte) (longNumber >> 24);
                payload[5] = (byte) (longNumber >> 16);
                payload[6] = (byte) (longNumber >> 8);
                payload[7] = (byte) longNumber;
                break;
        }
    }

    /**
     * 构造一个四字节数值
     *
     * @param value 数值
     */
    public FixedLengthNumber(int value) {
        this(value, NumberLength.FORE);
    }

    /**
     * 数值的可用长度
     *
     * @author YangGang
     * @date 2024/3/22
     */
    @Getter
    public enum NumberLength {
        ONE(1), FORE(4), EIGHT(8);
        /**
         * 长度
         */
        private final int len;

        NumberLength(int len) {
            this.len = len;
        }
    }

    public byte[] getPayload() {
        return payload;
    }
}
