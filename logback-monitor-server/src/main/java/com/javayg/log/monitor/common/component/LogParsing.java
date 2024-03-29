package com.javayg.log.monitor.common.component;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.javayg.common.constant.LogLevel;
import com.javayg.common.entity.CallChain;
import com.javayg.common.entity.Log;
import com.javayg.common.exception.UnknownLogLevelException;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

/**
 * 日志解析成功
 *
 * @author YangGang
 * @date 2024/3/4
 */
@Component
public class LogParsing {
    /**
     * 解析日志
     *
     * @date 2024/3/4
     * @author YangGang
     */
    public Log resolve(ByteBuffer buffer) throws UnknownLogLevelException {
        Log logInfo = new Log();
        // 日志级别
        byte level = buffer.get();
        logInfo.setLevel(LogLevel.getLogLevel(level));
        // 调用链
        CallChain callChain = new CallChain(buffer);
        logInfo.setCallChain(callChain);
        // 类名
        int loggerNameLen = buffer.getInt();
        byte[] loggerNameBytes = new byte[loggerNameLen];
        buffer.get(loggerNameBytes);
        logInfo.setLoggerName(new String(loggerNameBytes));
        // 线程名
        int threadNameBytesLen = buffer.getInt();
        byte[] threadNameBytesBytes = new byte[threadNameBytesLen];
        buffer.get(threadNameBytesBytes);
        logInfo.setThreadName(new String(threadNameBytesBytes));
        // 时间戳
        logInfo.setTimeStamp(buffer.getLong());
        // 日志消息
        int messageBytesLen = buffer.getInt();
        byte[] messageBytesBytes = new byte[messageBytesLen];
        buffer.get(messageBytesBytes);
        logInfo.setMessage(new String(messageBytesBytes));
        //异常信息
        int remaining = buffer.remaining();
        byte[] stackTraceBytesBytes = new byte[remaining];
        buffer.get(stackTraceBytesBytes);
        logInfo.setStackTrace(new String(stackTraceBytesBytes));
        return logInfo;
    }
}
