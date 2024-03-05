# logback-monitor logback 日志分析平台

## 介绍
logback-monitor-starter 是一个用于远程监控和管理 Logback 日志系统的工具。这个启动器扩展了 Logback 的功能，允许用户通过一个独立的、远程的监控界面来查看、分析和控制日志行为，而不需要直接访问应用程序的运行环境。

## 主要功能：
### 远程监控：
logback-monitor-starter 提供了一个独立的监控服务，可以通过网络远程访问。用户可以在任何可以访问该服务的设备上查看日志事件，无需在应用程序服务器上直接操作。

### 实时日志流：
通过 Socket 实时通信技术，该工具可以实时传输日志事件到监控界面，确保用户能够即时看到最新的日志信息。

### 日志过滤与搜索：
用户可以通过监控界面过滤和搜索日志事件，以便快速找到感兴趣的信息。这可以帮助用户快速定位问题或查找特定类型的日志条目。

### 配置动态更新：
除了查看日志，用户还可以通过远程界面动态更新 Logback 的配置。这意味着无需重启应用程序，就可以调整日志级别、添加或删除日志输出等。

### 性能统计与告警：
logback-monitor-starter 可以收集和分析日志系统的性能数据，并提供告警功能。当达到特定的阈值或检测到异常行为时，系统会发送告警通知。

### 集成与安全性：
该工具可以轻松地集成到现有的应用程序中，对于安全性，虽然后登录、用户管理、权限管理，但是一般只用于内网，万不可开放到外网，否则可能会有严重的安全隐患。

## 使用场景：
一般用于开发或测试环境，用于提升开发效率。
## 如何使用：
使用 logback-monitor-starter 通常需要以下步骤：

1. 添加依赖：在项目的 pom.xml 或 build.gradle 文件中添加 logback-monitor-starter 的依赖。

2. 配置监控服务：配置远程监控服务的地址、端口以及其他相关参数。

3. 集成到应用程序：如果是 Spring-Boot 项目可

4. 启动监控服务：启动远程监控服务，并确保应用程序能够连接到该服务。

5. 访问监控界面：通过浏览器或其他客户端访问远程监控服务的界面，开始监控和分析日志数据。
6. 初始密码：
- 用户名：yg 
  - 密码：test
- 用户名：admin2 
  - 密码：test

总之，remote-logback-monitor-starter 提供了一种方便、灵活的方式来远程监控和管理 Logback 日志系统，适用于分布式系统、多环境部署以及需要远程日志分析的场景。


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