package com.javayg.common.exception;

/**
 * 日志级别不匹配
 *
 * @author YangGang
 * @date 2024/3/1
 * @description 当处理日志事件时，无法根据 Log Event 的 LogLevel 确定该日志是什么级别的
 */
public class LogLevelMismatchException extends RuntimeException {
    public LogLevelMismatchException(String level) {
        super(String.format("找不到匹配的日志级别:【%s】", level));
    }
}
