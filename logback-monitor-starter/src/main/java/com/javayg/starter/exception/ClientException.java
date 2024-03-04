package com.javayg.starter.exception;

/**
 * 客户端失败
 *
 * @author YangGang
 * @date 2024/3/4
 * @description
 */
public class ClientException extends RuntimeException {
    public ClientException(String msg) {
        super(msg);
    }
}
