package com.javayg.common.exception;

/**
 * 网络异常
 *
 * @author YangGang
 * @date 2024/3/22
 */
public class ResponseUnknownException extends Exception {
    public ResponseUnknownException(String msg) {
        super(msg);
    }
}
