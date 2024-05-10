package com.javayg.common.utils;

/**
 * 字节码工具
 *
 * @author YangGang
 * @date 2024/3/4
 * @description
 */
public class ByteUtils {
    /**
     * 将字节数组转为 int 类型
     *
     * @param byteArray 字节数组
     * @return 对应的int值
     * @date 2024/3/4
     * @author YangGang
     */
    public static int byteArrayToInt(byte[] byteArray) {
        int value = 0;
        for (int i = 0; i < byteArray.length; i++) {
            value |= (byteArray[i] & 0xFF) << ((byteArray.length - 1 - i) * 8);
        }
        return value;
    }

    /**
     * 判断一个字节数组都是由0组成
     *
     * @param array 字节数组
     * @return 返回 true 表示所有元素都由 0 组成
     * @date 2024/3/29
     * @author YangGang
     */
    public static boolean isAllZeros(byte[] array) {
        for (byte b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }
}
