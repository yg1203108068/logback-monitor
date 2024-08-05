package com.javayg.log.monitor.common.entity.vo;

import com.javayg.log.monitor.common.constant.PushMessageType;
import lombok.Data;

/**
 * 心跳VO对象
 *
 * @author YangGang
 * Created on 2024/7/21
 */
@Data
public class HeartbeatVo implements PushMessage {
    /**
     * 推送的心跳类型
     */
    private PushMessageType messageType = PushMessageType.HEARTBEAT;
    /**
     * 在线的模块列表
     */
    private Integer[] modelList;

    public HeartbeatVo(Integer[] modelList) {
        this.modelList = modelList;
    }
}
