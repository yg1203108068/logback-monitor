import React, {useState} from "react";
import {Log, LogLevelColor} from "./index";
import {Affix, Avatar, Button, Popover, Switch, Typography} from "antd";
import {BarsOutlined, DeleteOutlined, LinkOutlined, VerticalAlignBottomOutlined} from "@ant-design/icons";

const {Paragraph, Text} = Typography;
const page = React.createRef();
export default class PassiveShowLogger extends React.Component<{
    serverId: number,
    registerLogAppender: (serverId: number, appendLog: (log: Log)=>void)=>void
}> {
    /**
     * 界面刷新计时器id
     */
    refreshPageIntervalId = undefined
    /**
     * 将被渲染的日志
     * @type {Log[]}
     */
    logs: Log[] = []

    state = {
        lastLogKey: -1,
    }

    appendLog = (log) => {
        this.logs.push(log)
    }

    clean = () => {
        this.logs = []
    }
    stopRefreshLog = (refresh) => {
        if (refresh) {
            this.startInterval();
        } else {
            clearInterval(this.refreshPageIntervalId)
            this.refreshPageIntervalId = undefined;
        }
    }

    componentDidMount = () => {
        this.startInterval();
        this.props.registerLogAppender(this.props.serverId, this.appendLog)
    }

    startInterval = () => {
        if (this.refreshPageIntervalId === undefined) {
            this.refreshPageIntervalId = setInterval(this.refreshPage, 1500);
        }
    }

    refreshPage = () => {
        if (this.logs.length === 0) {
            return;
        }
        const {autoRoll} = this.state;
        // 刷新日志
        this.setState({lastLogKey: this.logs[this.logs.length - 1].key}, () => {
            // 自动滚动
            if (autoRoll) {
                page.current.scrollTop = page.current.scrollHeight;
            }
        })
    }

    render() {
        return <div style={{height: "100%", overflowY: "auto", paddingRight: "10px"}} className={"custom-scrollbar-container"} ref={page}>
            <Affix key={"logAffix"} offsetTop={20} style={{textAlign: "right", height: 0}} target={() => page.current}>
                <Popover content={<PopoverContent clean={this.clean}
                                                  stopRefreshLog={this.stopRefreshLog}
                                                  setDivRoll={roll => {
                                                      console.log("设置滚动:", roll)
                                                      this.setState({
                                                          autoRoll: roll
                                                      })
                                                  }}
                />}
                         title={null}>
                    <Avatar style={{backgroundColor: 'rgba(24, 144, 255,0.5'}} shape="square" icon={<BarsOutlined/>}/>
                </Popover>
            </Affix>
            <p>当前共页共收集 {this.logs.length} 条数据</p>
            {(this.logs.length > 500 ? this.logs.slice(-500) : this.logs)
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
        </div>
    }
}

const PopoverContent = ({clean, setDivRoll, stopRefreshLog}) => {
    const [cleaning, setCleaning] = useState(false);
    const cleanOpt = () => {
        clean();
        setCleaning(true);
        setTimeout(() => {
            setCleaning(false)
        }, 3000)
    }
    return <div>
        <div style={{marginTop: "5px"}}><Switch onChange={stopRefreshLog}/><LinkOutlined/>接收日志</div>
        <br/>
        <div style={{marginTop: "5px"}}><Switch onChange={setDivRoll}/><VerticalAlignBottomOutlined/>自动滚动</div>
        <br/>
        <Button onClick={cleanOpt} loading={cleaning} key={"cleanBtn"} icon={<DeleteOutlined/>} type={"text"}>清空</Button>
    </div>
}