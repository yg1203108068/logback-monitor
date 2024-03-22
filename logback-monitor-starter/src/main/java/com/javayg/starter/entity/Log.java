package com.javayg.starter.entity;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.javayg.starter.exception.LogLevelMismatch;

import java.nio.ByteBuffer;

/**
 * 将要进行网络传输的日志对象
 *
 * @author YangGang
 * @date 2024/3/1
 * @description
 */
public class Log {
    /**
     * 日志级别
     *
     * @transfers 传输占用 5 位
     */
    private final LogLevel level;

    /**
     * 类名称
     *
     * @transfers 长度(int) + 类名称
     */
    private final String loggerName;

    /**
     * 线程名称
     *
     * @transfers 长度(int) + 线程名称
     */
    private final String threadName;
    /**
     * 时间戳
     *
     * @transfers Long 类型
     */
    private final Long timeStamp;

    /**
     * 消息
     *
     * @transfers 长度(long) + 消息
     */
    private String message;

    /**
     * 异常时的对堆栈信息
     *
     * @transfers 在数据包的末尾
     */
    private String stackTrace;

    /**
     * 获取要发送的数据包
     *
     * @return 一个完整的数据包
     * @date 2024/3/4
     * @author YangGang
     * @description
     */
    public byte[] payload() {
        byte[] loggerNameBytes = loggerName.getBytes();
        byte[] threadNameBytes = threadName.getBytes();
        byte[] messageBytes = message.getBytes();
        byte[] stackTraceBytes;
        if (stackTrace != null) {
            stackTraceBytes = stackTrace.getBytes();
        } else {
            stackTraceBytes = new byte[0];
        }
        int size = 1   // 日志级别
                + 4 + loggerNameBytes.length
                + 4 + threadNameBytes.length
                + 8  // 时间戳
                + 4 + messageBytes.length
                + stackTraceBytes.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 + size);
        buffer.putInt(size) // 先在负载的头部放一个4个字节的int值，这个值表示了消息体的长度
                .put(level.code)
                .putInt(loggerNameBytes.length)
                .put(loggerNameBytes)
                .putInt(threadNameBytes.length)
                .put(threadNameBytes)
                .putLong(timeStamp)
                .putInt(messageBytes.length)
                .put(messageBytes);
        if (stackTraceBytes.length > 0) {
            buffer.putInt(stackTraceBytes.length)
                    .put(stackTraceBytes);
        }
        return buffer.array();
    }

    /**
     * 日志级别枚举
     *
     * @author YangGang
     * @date 2024/3/1
     */
    public enum LogLevel {
        /**
         * ERROR
         */
        ERROR_INT(16),  //10000
        /**
         * WARN
         */
        WARN_INT(8),    //01000
        /**
         * INFO
         */
        INFO_INT(4),    //00100
        /**
         * DEBUG
         */
        DEBUG_INT(2),   //00010
        /**
         * TRACE
         */
        TRACE_INT(1);   // 00001
        private final byte code;

        LogLevel(int code) {
            this.code = (byte)code;
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
        private static LogLevel getLevel(String level) {
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
            throw new LogLevelMismatch(level);
        }
    }

    /**
     * 传输对象的构造 通过在日志事件的详细信息中精简需要的内容
     *
     * @param event 日志事件的详细信息
     */
    public Log(ILoggingEvent event) {
        this.level = LogLevel.getLevel(event.getLevel().levelStr);
        this.loggerName = event.getLoggerName();
        this.threadName = event.getThreadName();
        this.timeStamp = event.getTimeStamp();
        this.message = event.getMessage();
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        // 如果没有异常信息则结束
        if (throwableProxy == null) {
            return;
        }

        // 处理异常信息
        this.message = throwableProxy.getMessage();
        StackTraceElementProxy[] stackTraceElements = throwableProxy.getStackTraceElementProxyArray();
        if (stackTraceElements != null && stackTraceElements.length > 0) {

            StringBuilder sb = new StringBuilder();
            // 遍历堆栈跟踪并构建完整的堆栈信息
            for (StackTraceElementProxy elementProxy : stackTraceElements) {
                StackTraceElement element = elementProxy.getStackTraceElement();
                sb.append("\tat ").append(element).append("\n");
            }
            this.stackTrace = sb.toString();
        }
    }

    @Override
    public String toString() {
        return "Log{" +
                "level=" + level.code +
                ", loggerName='" + loggerName + '\'' +
                ", threadName='" + threadName + '\'' +
                ", timeStamp=" + timeStamp +
                ", message='" + message + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }
}
