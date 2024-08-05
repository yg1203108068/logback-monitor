package com.javayg.log.monitor.common.constant;

/**
 * 请求消息类型
 *
 * @author YangGang
 * Created on 2024/7/21
 */
public enum PushMessageType {
    /**
     * 正常的，日志消息
     */
    NORMAL,
    /**
     * 错误的，系统异常（非远程日志，而是本地异常）
     */
    ERROR,
    /**
     * 警告的，系统异常（非远程日志，而是本地异常）
     */
    WARN,
    /**
     * 心跳
     */
    HEARTBEAT;
}
