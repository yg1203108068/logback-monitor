package com.javayg.log.monitor.common.entity.net.wapper;

import lombok.Getter;

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
     * 构造一个固定长度的数值类型
     *
     * @param value  数值
     * @param len 数值的长度
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
