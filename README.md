# logback-monitor logback 日志分析平台

#### 介绍
一个实时日志分析平台，提供了 starter 和 服务端。实现简单，方便二开。

#### 软件架构
logback-monitor-starter 


#### 安装教程
1. 下载 `https://gitee.com/java_yg/logback-monitor.git` 代码，启动 logback-monitor-server 项目。
2. 在需要分析的软件中添加依赖
```xml
<dependency>
   <groupId>com.javayg.starter</groupId>
   <artifactId>logback-monitor-starter</artifactId>
   <version>1.0.3</version>
</dependency>
```
3. 在需要分析的软件中添加配置
``` yml
logback:
  monitor:
    host: 127.0.0.1
    port: 8733
```
4. 启动项目

#### 常见问题

1.  如果 starter 启动失败，可以考虑在 logback.xml 中修改 `<configuration>` 添加 debug 属性。改为 `<configuration debug="true">`