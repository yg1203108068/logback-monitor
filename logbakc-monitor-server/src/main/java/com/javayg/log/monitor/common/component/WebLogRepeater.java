package com.javayg.log.monitor.common.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日志中继器
 *
 * @author YangGang
 * @date 2024/2/26
 * @description 将接收到的日志发送给Web浏览器
 */
@Component
@Slf4j
public class WebLogRepeater extends OutputStream {
    //    private static WebLogRepeater outputStream;
    private final Map<String, FluxSink<String>> clients = new LinkedHashMap<>();
    // key：客户端Id，value：接入时间
    private final Map<String, Long> timer = new HashMap<>();

    public void addClient(String id, FluxSink<String> client) {
        if (clients.size() >= 10) {
            String firstClientId = clients.keySet().iterator().next();
            removeClient(firstClientId + "服务端存在过多负载,主动断开链接,如有需要请重新打开链接。", firstClientId);
        }
        clients.put(id, client);
        timer.put(id, System.currentTimeMillis());
        log.info("添加客户端ID:{},当前客户端总数{}", id, clients.size());
    }

    public void removeClient(String endMsg, String id) {
        FluxSink<String> client = clients.get(id);
        if (StrUtil.isNotBlank(endMsg)) {
            client.next(endMsg);
        }
        client.complete();
        clients.remove(id);
        timer.remove(id);
    }

    @Override
    public void write(int b) {
    }

    @Override
    public void write(byte[] b) {
        String msg = new String(b).replaceAll("(\\r\\n|\\n|\\n\\r)", "<br>");
        if (CollectionUtil.isNotEmpty(clients.values())) {
            Iterator<String> iterator = clients.keySet().iterator();
            while (iterator.hasNext()) {
                String clientId = iterator.next();
                // 30分钟超时
                if (System.currentTimeMillis() - timer.get(clientId) > 1800000) {
                    removeClient(clientId + "链接已经超过30分钟,自动断开链接,如有需要请重新打开链接", clientId);
                } else {
                    clients.get(clientId).next(msg);
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (CollectionUtil.isNotEmpty(clients.values())) {
            clients.values().forEach(FluxSink::complete);
        }
        super.close();
    }

    //    public static WebLogRepeater getInstance() {
    //        if (outputStream == null) {
    //            outputStream = new WebLogRepeater();
    //        }
    //        return outputStream;
    //    }
}
