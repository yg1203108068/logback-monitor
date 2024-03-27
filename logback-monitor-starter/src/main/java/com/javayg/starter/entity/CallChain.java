package com.javayg.starter.entity;

import lombok.Data;

/**
 * 调用链实体
 *
 * @author YangGang
 * @date 2024/3/26
 */
@Data
public class CallChain {
    /**
     * 调用链id
     */
    private String id;
    /**
     * 上一个客户端id
     */
    private String prevClientId;

    /**
     * 构造一个调用链
     *
     * @param prevClientId 上一个客户端id
     */
    public CallChain(String prevClientId, String id) {
        this.prevClientId = prevClientId;
        this.id = id;
    }

}
