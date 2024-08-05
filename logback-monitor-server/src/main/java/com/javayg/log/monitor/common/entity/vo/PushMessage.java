package com.javayg.log.monitor.common.entity.vo;

import com.javayg.log.monitor.common.constant.PushMessageType;

/**
 * 向客户端(浏览器)推送消息的类型
 *
 * @author YangGang
 * Created on 2024/7/21
 */
public interface PushMessage {
    PushMessageType getMessageType();
}
