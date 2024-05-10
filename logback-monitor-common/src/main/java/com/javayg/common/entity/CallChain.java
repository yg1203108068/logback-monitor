package com.javayg.common.entity;

import com.javayg.common.utils.ByteUtils;
import com.javayg.common.wrapper.VariableLengthString;
import lombok.Data;

import java.nio.ByteBuffer;

/**
 * 调用链实体
 *
 * @author YangGang
 * @date 2024/3/26
 */
@Data
public class CallChain {
    /**
     * 主调用链id
     */
    private String mainId;
    /**
     * 当前链id
     */
    private String id;
    /**
     * 上一个客户端id
     */
    private String prevClientId;
    /**
     * 地址
     */
    private String pathInfo;

    public CallChain() {

    }

    /**
     * 构造一个调用链
     *
     * @param prevClientId 上一个客户端id
     */
    public CallChain(String prevClientId, String id, String mainCallChainId, String pathInfo) {
        this.prevClientId = prevClientId;
        this.mainId = mainCallChainId;
        this.id = id;
        this.pathInfo = pathInfo;
    }

    /**
     * 通过缓冲区构建调用链
     *
     * @param buffer 缓冲区
     */
    public CallChain(ByteBuffer buffer) {
        byte[] mainIdBytes = new byte[36];
        // 主调用链id
        buffer.get(mainIdBytes);
        if (!ByteUtils.isAllZeros(mainIdBytes)) {
            mainId = new String(mainIdBytes);
        }

        // 上一个客户端id
        VariableLengthString prevClientIdStr = new VariableLengthString(buffer);
        prevClientId = prevClientIdStr.getContent();

        // 当前调用链id
        byte[] idBytes = new byte[36];
        buffer.get(idBytes);
        if (!ByteUtils.isAllZeros(mainIdBytes)) {
            id = new String(idBytes);
        }

        // 当前路径
        VariableLengthString path = new VariableLengthString(buffer);
        pathInfo = path.getContent();
    }

    /**
     * 获取调用链对象的网络负载 mainId(36) + prevClientId(VariableLengthString) + id(36) + pathInfo(VariableLengthString)
     *
     * @return 字节数组的网络负载
     * @date 2024/3/28
     * @author YangGang
     */
    public byte[] payload() {
        byte[] prevClientIdBytes = new VariableLengthString(prevClientId).getContentBytes();
        byte[] pathInfoStringBytes = new VariableLengthString(pathInfo).getContentBytes();

        ByteBuffer buffer = ByteBuffer.allocate(72 + prevClientIdBytes.length + pathInfoStringBytes.length);

        // 主调用链id
        if (mainId == null) {
            buffer.put(new byte[36]);
        } else {
            buffer.put(mainId.getBytes());
        }
        // 上一个客户端id
        if (prevClientId != null && !prevClientId.isEmpty()) {
            buffer.put(prevClientIdBytes);
        } else {
            buffer.put(new VariableLengthString().getContentBytes());
        }

        // 当前调用链id
        if (id == null) {
            buffer.put(new byte[36]);
        } else {
            buffer.put(id.getBytes());
        }

        // 当前路径
        if (pathInfo != null) {
            buffer.put(new VariableLengthString(pathInfo).getContentBytes());
        } else {
            buffer.put(new VariableLengthString().getContentBytes());
        }

        return buffer.array();
    }

}
