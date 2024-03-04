package com.javayg.starter.exception;

/**
 * 修复连接失败
 *
 * @author YangGang
 * @date 2024/3/4
 * @description
 */
public class FixSocketException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public FixSocketException(String message) {
        super(message);
    }
}
