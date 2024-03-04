import React, {Component} from "react";
import {Affix, Avatar, Button, Popover, Switch, Typography} from "antd";
import {BarsOutlined, DeleteOutlined, LinkOutlined, VerticalAlignBottomOutlined} from "@ant-design/icons";

const {Paragraph, Text} = Typography;
type ShowLoggerState = {
    logData: Log[]
}
type Log = {
    key: number,
    log: string,
    leave: string,
}
let eventSource;
const loggerContainer = React.createRef();
export default class ShowLogger extends Component {
    state: ShowLoggerState = {
        errorLog: [],
        logData: [],
        autoRoll: true,
        errorStatus: false,
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
            errorLog: [],
            logData: [],
        })
    }

    toConnection = () => {
        if (eventSource != undefined) {
            eventSource.close();
        }

        eventSource = new EventSource(`/api/log/output`);

        eventSource.addEventListener("message", e => {
            const {autoRoll, logData, errorLog} = this.state;
            let leave = e.data.indexOf(" INFO ") !== -1 ? "INFO" : e.data.indexOf(" ERROR ") !== -1 ? "ERROR" : "";
            let log = {
                key: 'Log_Msg_' + logData.length,
                log: e.data.replace(/<br>/g, '\n'),
                leave
            };
            logData.push(log);
            if (leave === "ERROR") {
                errorLog.push(log);
            }
            this.setState({logData}, () => {
                if (autoRoll) {
                    document.getElementById("logContent").parentElement.scrollTop = document.getElementById("logContent").offsetHeight;
                }
            })
        });
    }
    setOnlyError = (errorStatus) => {
        this.setState({errorStatus})
    }
    acceptNotification = (notificationStatus) => {
        this.setState({notificationStatus})
    }

    render() {
        const {errorStatus, errorLog, logData} = this.state;
        const logs = (errorStatus === true ? errorLog : logData).map(item => {
            // 处理 info 日志
            if (item.leave === "INFO") {
                let index = item.log.indexOf(" INFO ");
                return <Paragraph key={item.key}>
                    {item.log.substring(0, index)}
                    <Text type="success"> INFO </Text>
                    {item.log.substring(index + 6, item.log.length)}
                </Paragraph>;
            }
            // 处理 error 日志
            if (item.leave === "ERROR") {
                return <Paragraph key={item.key}>
                    <Text type="danger">{item.log}</Text>
                </Paragraph>;
            }
            return <Paragraph key={item.key}>{item.log}</Paragraph>;
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