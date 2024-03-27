package com.javayg.starter.constant;

import com.javayg.starter.exception.ResponseUnknownException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网络状态
 *
 * @author YangGang
 * @date 2024/3/4
 */
@Getter
@AllArgsConstructor
public enum Status {
    /**
     * 成功状态
     */
    SUCCESS((byte) 0b1),
    WARN((byte) 0b10),
    ERROR((byte) 0b100),
    ;

    private byte code;

    /**
     * 通过一字节状态码获取状态对象
     *
     * @param code 状态码
     * @return 一个已知的响应状态
     * @throws ResponseUnknownException 无法确定响应状态
     * @date 2024/3/22
     * @author YangGang
     */
    public static Status getInstance(byte code) throws ResponseUnknownException {
        for (Status value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new ResponseUnknownException("响应异常，无法确定响应状态");
    }
}