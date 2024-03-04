package com.javayg.starter.debug;


public class Debug {
    /**
     * 打印byte的二进制到控制台
     *
     * @param value 二进制数
     * @date 2023/12/5
     * @author YangGang
     * @description 一个byte将打印8位，不足8位时高位补零，四位一个空格
     */
    public static void printBinary(byte value) {
        // 通过位运算获取每一位的值，并输出到控制台
        for (int i = 7; i >= 0; i--) {
            // 右移i位，然后与1进行与运算，获取第i位的值
            int bit = (value >> i) & 1;
            System.out.print(bit);

            // 每四位输出一个空格
            if (i % 4 == 0) {
                System.out.print(" ");
            }
        }
        System.out.println(); // 换行
    }

    /**
     * 打印byte数组
     *
     * @param bytes 需要打印的byte数组
     * @date 2023/12/6
     * @author YangGang
     * @description
     */
    public static void printBinaryArr(byte[] bytes) {
        for (byte b : bytes) {
            printBinary(b);
        }
    }

    /**
     * 打印一个数字的二进制到控制台
     *
     * @param value 一个数字
     * @date 2023/12/5
     * @author YangGang
     * @description 打印出这个数字的二进制，当高位字节不足8位时补零，四位一个下划线，八位一个空值，不适合直接打印数据包
     */
    public static void printIntToBinary(int value) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((value >> i) & 1);
            if (i % 8 == 0) {
                System.out.print(" ");
                continue;
            }
            if (i % 4 == 0) {
                System.out.print("_");
            }
        }
    }
}
