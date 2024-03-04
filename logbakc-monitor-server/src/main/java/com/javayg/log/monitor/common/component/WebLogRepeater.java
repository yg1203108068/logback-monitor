package com.javayg.log.monitor.common.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.javayg.log.monitor.common.entity.log.Log;
import com.javayg.log.monitor.common.entity.vo.OutputVO;
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
        log.info("添加客户端ID:{},当前显示客户端总数{}", id, clients.size());
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

    /**
     * 输出信息
     *
     * @param vo 被输出的对象
     * @date 2024/3/4
     * @author YangGang
     * @description
     */
    public void write(OutputVO vo) {
        if (CollectionUtil.isNotEmpty(clients.values())) {
            Iterator<String> iterator = clients.keySet().iterator();
            while (iterator.hasNext()) {
                String clientId = iterator.next();
                // 30分钟超时
                if (System.currentTimeMillis() - timer.get(clientId) > 1800000) {
                    removeClient(clientId + "链接已经超过30分钟,自动断开链接,如有需要请重新打开链接", clientId);
                } else {
                    clients.get(clientId).next(JSONUtil.toJsonStr(vo));
                }
            }
        }
    }

    /**
     * 处理日志信息
     *
     * @param logInfo 日志信息
     * @date 2024/3/4
     * @author YangGang
     * @description
     */
    public void logHandler(Log logInfo) {
        OutputVO vo = new OutputVO();
        vo.setStatus(200);
        vo.setData(logInfo);
        write(vo);
    }

    @Override
    public void close() throws IOException {
        if (CollectionUtil.isNotEmpty(clients.values())) {
            clients.values().forEach(FluxSink::complete);
        }
        super.close();
    }

    /**
     * 推送一个错误
     *
     * @param msg 错误信息
     * @date 2024/3/4
     * @author YangGang
     * @description
     */
    public void error(String msg) {
        OutputVO vo = new OutputVO();
        vo.setStatus(500);
        vo.setMsg(msg);
        write(vo);
    }

    /**
     * 推送一个警告
     *
     * @param msg 警告信息
     * @date 2024/3/4
     * @author YangGang
     * @description
     */
    public void warn(String msg) {
        OutputVO vo = new OutputVO();
        vo.setStatus(501);
        vo.setMsg(msg);
        write(vo);
    }

}
