package com.javayg.log.monitor.common.entity.log;

import com.javayg.log.monitor.common.constant.LogLevel;
import lombok.Data;

/**
 * 将要进行网络传输的日志对象
 *
 * @author YangGang
 * @date 2024/3/1
 * @description
 */
@Data
public class Log {
    /**
     * 日志级别
     *
     * @transfers 传输占用 5 位
     */
    private LogLevel level;

    /**
     * 类名称
     *
     * @transfers 长度(int) + 类名称
     */
    private String loggerName;

    /**
     * 线程名称
     *
     * @transfers 长度(int) + 线程名称
     */
    private String threadName;
    /**
     * 时间戳
     *
     * @transfers Long 类型
     */
    private Long timeStamp;

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

}
