import React, {Component} from "react";
import {Affix, Avatar, Button, Popover, Switch, Typography} from "antd";
import {BarsOutlined, DeleteOutlined, LinkOutlined, VerticalAlignBottomOutlined} from "@ant-design/icons";

const {Paragraph, Text} = Typography;
export type Log = {
    key: number,
    time: string,
    loggerName: string,
    level: string,
    threadName: string,
    timeStamp: number,
    message: string,
    stackTrace: string,
}

const STATUS = {
    SUCCESS: 200,
    ERROR: 500,
    WARN: 501
}

type LogResponse = {
    data: Log,// 日志数据
    msg: string, // 响应消息
    status: number, // 响应状态
    serverId: number, // 推送这条消息的服务id
}
const LogLevelColor = {
    16: {text: "ERROR", color: "danger"},
    8: {text: "WARN", color: "warning"},
    4: {text: "INFO", color: "success"},
    2: {text: "DEBUG", color: "secondary"},
    1: {text: "TRACE", color: "default"}
}


/**
 * 显示日志组件中的 state 接口包含的内容
 */
interface ShowLoggerState {
    logData: Log[], // 非错误日志集合
    autoRoll: boolean,// 自动滚动
    errorStatus: boolean, // 当前连接的错误状态
    socketErrorLog: string[], // 当前连接的错误信息
    notificationStatus: boolean,// 手动控制当前连接是否继续接收通知
}

let eventSource;
const loggerContainer = React.createRef();
export default class ShowLogger extends Component {
    state: ShowLoggerState = {
        logData: [],
        autoRoll: true,
        errorStatus: false,
        socketErrorLog: [],
        notificationStatus: false,
    }

    componentDidMount() {
        this.toConnection();
        console.log("日志服务连接建立")
    }

    componentWillUnmount() {
        try {
            eventSource.close();
            console.log("日志服务连接关闭")
        } catch (error) {
            console.error("关闭日志服务链接失败");
        }
    }

    clean = () => {
        this.setState({
            socketErrorLog: [],
            logData: [],
        })
    }

    toConnection = () => {
        if (eventSource != undefined) {
            eventSource.close();
        }

        try {
            eventSource = new EventSource(`/api/log/output`);

            eventSource.addEventListener("message", event => {
                const {autoRoll, logData, socketErrorLog} = this.state;
                try {
                    let logResponse: LogResponse = JSON.parse(event.data);
                    if (logResponse.status === STATUS.SUCCESS) {
                        let log: Log = {
                            ...logResponse.data,
                            key: `${logResponse.serverId}-${logData.length}`,
                            time: new Date(logResponse.data.timeStamp).toLocaleString()
                        };
                        logData.push(log);
                        // 刷新日志
                        this.setState({logData}, () => {
                            // 自动滚动
                            if (autoRoll) {
                                document.getElementById("logContent").parentElement.scrollTop = document.getElementById("logContent").offsetHeight;
                            }
                        })
                    } else {
                        socketErrorLog.push(`错误代码：${logResponse.status}, 错误消息：${logResponse.msg}`);
                        this.setState({socketErrorLog});
                    }
                } catch (e) {
                    console.error(e);
                    socketErrorLog.push("解析日志失败");
                    this.setState({socketErrorLog});
                }
            });
        } catch (e) {
            console.error(e);
            const {socketErrorLog} = this.state;
            socketErrorLog.push("建立连接失败");
            this.setState({socketErrorLog});
        }
    }
    setOnlyError = (errorStatus) => {
        this.setState({errorStatus})
    }
    acceptNotification = (notificationStatus) => {
        this.setState({notificationStatus})
    }

    render() {
        const {errorStatus, socketErrorLog, logData} = this.state;
        const logs = (errorStatus === true ? socketErrorLog : logData).map(item => {
            return <Paragraph key={item.key}>
                {item.time}&nbsp;
                {item.loggerName}&nbsp;
                [{item.threadName}]&nbsp;
                <Text type={LogLevelColor[item.level].color}> {LogLevelColor[item.level].text} </Text>
                {item.level === 16 ? item.message + item.stackTrace : item.message}
            </Paragraph>;
        });
        return <div style={{height: "100%", overflowY: "scroll", paddingRight: "10px"}} className={"custom-scrollbar-container"} ref={loggerContainer}>
            <Affix key={"logAffix"} offsetTop={20} style={{textAlign: "right"}} target={() => loggerContainer.current}>
                <Popover content={<PopoverContent clean={this.clean} relink={this.toConnection} acceptNotification={this.acceptNotification} setOnlyError={this.setOnlyError}
                                                  setDivRoll={roll => {
                                                      console.log("设置滚动:", roll)
                                                      this.setState({
                                                          autoRoll: roll
                                                      })
                                                  }}/>} title={null}>
                    <Avatar style={{backgroundColor: '#1890ff'}} shape="square" icon={<BarsOutlined/>}/>
                </Popover>
            </Affix>

            <div key={"logContent"} id={"logContent"} style={{whiteSpace: "pre-wrap"}}
                 className={"custom-scrollbar-container"}>
                {logs}
            </div>
        </div>;
    }
}
const PopoverContent = ({clean, relink, setDivRoll, acceptNotification, setOnlyError}) => {
    return <div>
        <div><Switch onChange={setOnlyError}/><LinkOutlined/>只显示错误</div>
        <br/>
        <div style={{marginTop: "5px"}}><Switch onChange={acceptNotification}/><LinkOutlined/>接收通知</div>
        <br/>
        <div style={{marginTop: "5px"}}><Switch defaultChecked onChange={setDivRoll}/><VerticalAlignBottomOutlined/>自动滚动</div>
        <br/>
        <Button onClick={clean} key={"cleanBtn"} icon={<DeleteOutlined/>} type={"text"}>清空</Button>
        <Button onClick={relink} key={"relinkBtn"} icon={<LinkOutlined/>} type={"text"}>重新链接</Button>
    </div>
}