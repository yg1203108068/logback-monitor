package com.javayg.log.monitor.common.exception;

/**
 * 未知的日志级别异常
 *
 * @author YangGang
 * @date 2024/3/4
 * @description
 */
public class UnknownLogLevelException extends Exception {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UnknownLogLevelException() {
        super("未知的日志级别");
    }
}
