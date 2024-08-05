import React, {Component} from "react";
import {Affix, Avatar, Button, Popover, Switch, Typography} from "antd";
import {BarsOutlined, DeleteOutlined, LinkOutlined, VerticalAlignBottomOutlined} from "@ant-design/icons";

const {Paragraph, Text} = Typography;
export type Log = {
    key: number,
    time?: string,
    loggerName?: string,
    level?: number,
    threadName?: string,
    timeStamp?: number,
    message: string,
    stackTrace?: string,
}

export const STATUS = {
    SUCCESS: 'NORMAL',
    HEARTBEAT: 'HEARTBEAT',
    ERROR: 'ERROR',
    WARN: 'WARN'
}

export type LogResponse = {
    data: Log,// 日志数据
    msg: string, // 响应消息
    messageType: number, // 消息类型
    serverId: number, // 推送这条消息的服务id
}
export const LogLevelColor = {
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
const page = React.createRef();
const logData: Log[] = [];
// 界面刷新计时器id
let refreshPageIntervalId;
export default class ShowLogger extends Component<{}> {
    state: ShowLoggerState = {
        autoRoll: false,
        errorStatus: false,
        socketErrorLog: [],
        notificationStatus: false,
        lastLogKey: -1,
    }

    componentDidMount() {
        this.toConnection();
        console.log("日志服务连接建立")
    }


    refreshPage = () => {
        const {autoRoll} = this.state;
        // 刷新日志
        if (logData.length === 0) {
            return
        }
        this.setState({lastLogKey: logData[logData.length - 1].key}, () => {
            // 自动滚动
            if (autoRoll) {
                page.current.scrollTop = page.current.scrollHeight;
            }
        })
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
        logData.length = 0;
        this.setState({
            socketErrorLog: [],
        })
    }

    toConnection = () => {
        if (eventSource != undefined) {
            eventSource.close();
        }

        try {
            eventSource = new EventSource(`/api/log/output`);
            eventSource.addEventListener("message", event => {
                const {socketErrorLog} = this.state;
                try {
                    let logResponse: LogResponse = JSON.parse(event.data);
                    if (logResponse.messageType === STATUS.SUCCESS) {
                        let log: Log = {
                            ...logResponse.data,
                            key: `${logResponse.serverId}-${logData.length}`,
                            time: new Date(logResponse.data.timeStamp).toLocaleString()
                        };
                        logData.push(log);
                    } else if (logResponse.messageType === STATUS.HEARTBEAT) {
                        console.log("心跳 +1", logResponse)
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
            if (refreshPageIntervalId === undefined) {
                refreshPageIntervalId = setInterval(this.refreshPage, 1500);
            }

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
        if (notificationStatus === false) {
            eventSource.close();
            this.refreshPage();
            clearInterval(refreshPageIntervalId)
            refreshPageIntervalId = undefined;
        } else {
            this.toConnection();
        }
    }

    render() {
        const {errorStatus, socketErrorLog} = this.state;
        return <div style={{height: "100%", overflowY: "auto", paddingRight: "10px"}} className={"custom-scrollbar-container"} ref={page}>
            <Affix key={"logAffix"} offsetTop={20} style={{textAlign: "right", height: 0}} target={() => page.current}>
                <Popover content={<PopoverContent clean={this.clean}
                                                  relink={this.toConnection}
                                                  acceptNotification={this.acceptNotification}
                                                  setOnlyError={this.setOnlyError}
                                                  setDivRoll={roll => {
                                                      console.log("设置滚动:", roll)
                                                      this.setState({
                                                          autoRoll: roll
                                                      })
                                                  }}/>}
                         title={null}>
                    <Avatar style={{backgroundColor: 'rgba(24, 144, 255,0.5'}} shape="square" icon={<BarsOutlined/>}/>
                </Popover>
            </Affix>
            <p>当前共页共收集 {logData.length} 条数据</p>
            {(logData.length > 500 ? logData.slice(-500) : logData)
                .map((item: Log) => (
                    <Paragraph key={`Paragraph-${item.key}`}>
                        {item.key}&nbsp;
                        {item.time}&nbsp;
                        {item.loggerName}&nbsp;
                        [{item.threadName}]&nbsp;
                        <Text key={`Paragraph-Text-${item.key}`} type={LogLevelColor[item.level].color}> {LogLevelColor[item.level].text} </Text>
                        {item.level === 16 ? item.message + item.stackTrace : item.message}
                    </Paragraph>
                ))}
        </div>;
    }
}
const PopoverContent = ({clean, relink, setDivRoll, acceptNotification, setOnlyError}) => {
    return <div>
        <div><Switch onChange={setOnlyError}/><LinkOutlined/>只显示错误</div>
        <br/>
        <div style={{marginTop: "5px"}}><Switch onChange={acceptNotification}/><LinkOutlined/>接收日志</div>
        <br/>
        <div style={{marginTop: "5px"}}><Switch onChange={setDivRoll}/><VerticalAlignBottomOutlined/>自动滚动</div>
        <br/>
        <Button onClick={clean} key={"cleanBtn"} icon={<DeleteOutlined/>} type={"text"}>清空</Button>
        <Button onClick={relink} key={"relinkBtn"} icon={<LinkOutlined/>} type={"text"}>重新链接</Button>
    </div>
}