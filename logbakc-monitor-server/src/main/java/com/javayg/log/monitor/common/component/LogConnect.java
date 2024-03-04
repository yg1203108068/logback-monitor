package com.javayg.log.monitor.common.component;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 日志连接类
 *
 * @author YangGang
 * @date 2024/2/26
 * @description 用于接管远程客户端。
 */
public class LogConnect extends Thread {

    private final WebLogRepeater webLogRepeater;
    private final Socket remote;
    private boolean connected = true;
    private final ClientCloseCallBack clientCloseCallBack;

    public LogConnect(Socket remote, WebLogRepeater webLogRepeater, ClientCloseCallBack clientCloseCallBack) {
        this.remote = remote;
        this.webLogRepeater = webLogRepeater;
        this.clientCloseCallBack = clientCloseCallBack;
    }

    @Override
    public void run() {
        try {
            super.run();
            // 不用向客户端发送消息
            while (connected) {
                // 循环读取客户端发送来的日志信息
                InputStream inputStream = remote.getInputStream();
                byte[] bytes = new byte[1024];
                int len = inputStream.read(bytes);
                ByteBuffer buffer = ByteBuffer.allocate(len);
                buffer.put(bytes, 0, len);
                String s = new String(buffer.array(), StandardCharsets.UTF_8);
                // 准备关闭
                if (s.equals("SHUTDOWN")) {
                    connected = false;
                    System.out.println("断开连接");
                    clientCloseCallBack.close(this);
                    continue;
                }

                System.out.print("Remote : " + s);
                // 将客户端信息处理后发送给日志中继器
                webLogRepeater.write(buffer.array());
            }
        } catch (SocketException e) {
            System.out.println("已经与 客户端 Socket 断开连接");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        System.out.println("服务端主动关闭连接");
        try {
            remote.getOutputStream().write("SHUTDOWN".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connected = false;
        try {
            remote.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 启动重连监听
        new Thread(() -> {

        }).start();
    }
}
