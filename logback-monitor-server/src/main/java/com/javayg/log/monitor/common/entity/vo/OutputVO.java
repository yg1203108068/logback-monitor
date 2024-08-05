package com.javayg.log.monitor.common.entity.vo;

import com.javayg.common.entity.Log;
import com.javayg.log.monitor.common.constant.PushMessageType;
import lombok.Data;

/**
 * 日志页面实时输出的VO对象
 *
 * @author YangGang
 * @date 2024/3/4
 */
@Data
public class OutputVO implements PushMessage {
    /**
     * 日志信息
     */
    private Log data;
    /**
     * 消息类型
     */
    private PushMessageType messageType;
    /**
     * 消息
     */
    private String msg;

    /**
     * 推送消息的服务id
     */
    private int serverId;
}
