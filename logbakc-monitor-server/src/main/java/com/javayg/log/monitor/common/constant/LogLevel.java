package com.javayg.log.monitor.common.constant;

import com.javayg.log.monitor.common.exception.UnknownLogLevelException;

/**
 * 日志级别枚举
 *
 * @author YangGang
 * @date 2024/3/1
 */
public enum LogLevel {
    ERROR_INT((byte)16),  //10000
    WARN_INT((byte)8),    //01000
    INFO_INT((byte)4),    //00100
    DEBUG_INT((byte)2),   //00010
    TRACE_INT((byte)1);   // 00001
    private final byte code;

    LogLevel(byte code) {
        this.code = code;
    }

    public static LogLevel getLogLevel(byte code) throws UnknownLogLevelException {
        switch (code) {
            case 1:
                return TRACE_INT;
            case 2:
                return DEBUG_INT;
            case 4:
                return INFO_INT;
            case 8:
                return WARN_INT;
            case 16:
                return ERROR_INT;
        }
        throw new UnknownLogLevelException();
    }
}