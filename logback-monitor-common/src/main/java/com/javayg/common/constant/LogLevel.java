package com.javayg.common.constant;

import com.javayg.common.exception.LogLevelMismatchException;
import com.javayg.common.exception.UnknownLogLevelException;
import lombok.Getter;

/**
 * 日志级别枚举
 *
 * @author YangGang
 * @date 2024/3/1
 */
@Getter
public enum LogLevel {
    ERROR_INT((byte) 16),  //10000
    WARN_INT((byte) 8),    //01000
    INFO_INT((byte) 4),    //00100
    DEBUG_INT((byte) 2),   //00010
    TRACE_INT((byte) 1);   // 00001
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

    /**
     * 通过 公认的 日志级别 的关键字进行区分日志级别
     *
     * @param level 日志级别的关键字
     * @return 日志级别
     * @date 2024/3/1
     * @author YangGang
     * @description ERROR、WARN、INFO、DEBUG、TRACE
     */
    public static LogLevel getLevel(String level) {
        if (level.equals("ERROR")) {
            return ERROR_INT;
        } else if (level.equals("WARN")) {
            return WARN_INT;
        } else if (level.equals("INFO")) {
            return INFO_INT;
        } else if (level.equals("DEBUG")) {
            return DEBUG_INT;
        } else if (level.equals("TRACE")) {
            return TRACE_INT;
        }
        throw new LogLevelMismatchException(level);
    }
}