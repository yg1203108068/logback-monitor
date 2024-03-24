import React from "react";
import {axiosUtils} from "../../utils/axiosUtils";
import {notification, Spin, Switch, Tabs} from "antd";
import {isEmpty} from "../../utils/strUtils";
import {FetchDataResult} from "../../constant/commonComponents";
import PassiveShowLogger from "../log/passiveShowLogger";
import type {LogResponse} from "../log";
import {Log, STATUS} from "../log";


type LogWindow = {
    serverId: number,
    serverName: string,
    host: string,
    port: number,
}
let serverJoinCheckIntervalId;
const logComponents = {}
let eventSource;
let noticed = [];
export default class ColonyLog extends React.Component {
    state = {
        tabs: [],
        loading: true,
        requestStatus: {code: 200},
        colorBgContainer: undefined
    }

    componentDidMount() {
        this.load();
    }

    /**
     * 检查服务接入
     */
    serverJoinCheck = (check) => {
        if (check) {
            serverJoinCheckIntervalId = setInterval(this.load, 5000);
        } else {
            clearInterval(serverJoinCheckIntervalId);
            serverJoinCheckIntervalId = undefined;
        }
    }

    load = () => {
        axiosUtils.get("/api/colony/list", (data: LogWindow[]) => {
            let tabs = [];
            data.map(item => {
                let key = `Log-Tab-${item.serverId}`, label,
                    children = <div style={{height: "100%", overflowY: "auto", paddingRight: "10px"}}>
                        <PassiveShowLogger
                            key={`Log-Tab-ShowLogger-${item.serverId}`}
                            serverId={item.serverId}
                            registerLogAppender={(serverId, appendLog) => logComponents[serverId] = appendLog}
                        />
                    </div>;
                if (!isEmpty(item.serverName)) {
                    label = item.serverName
                } else {
                    label = `${item.host}:${item.port}`
                }
                tabs.push({key, label, children})
            });
            if (tabs.length === 0) {
                return
            }
            this.setState({loading: false, requestStatus: {code: 200, msg: "成功"}, tabs}, () => {
                this.toConnection()
            })
        }, (code, msg) => {
            this.setState({
                requestStatus: {code, msg}
            });
        });
    }

    render() {
        const {tabs, loading, requestStatus} = this.state;
        if (requestStatus.code !== 200) {
            return <FetchDataResult data={requestStatus}/>
        }
        return <div>
            <Switch onChange={this.serverJoinCheck}/>自动扫描可渲染的日志窗口
            <Spin spinning={loading}>
                <Tabs items={tabs}/>
            </Spin>
        </div>;
    }


    componentWillUnmount() {
        try {
            eventSource.close();
            console.log("日志服务连接关闭")
        } catch (error) {
            console.error("关闭日志服务链接失败");
        }
    }


    toConnection = () => {
        if (eventSource !== undefined) {
            eventSource.close();
        }

        try {
            eventSource = new EventSource(`/api/log/output`);

            eventSource.addEventListener("message", event => {
                try {
                    let logResponse: LogResponse = JSON.parse(event.data);
                    if (logResponse.status === STATUS.SUCCESS) {
                        //判断窗口是否已经渲染
                        if (logComponents[logResponse.serverId] === undefined) {
                            return
                        }

                        let log: Log = {
                            ...logResponse.data,
                            key: `${logResponse.serverId}-${Math.random()}`,
                            time: new Date(logResponse.data.timeStamp).toLocaleString()
                        };
                        logComponents[logResponse.serverId](log)
                    } else {
                        console.log("被丢弃的日志信息：", logResponse)
                        let now = new Date();
                        let timeStamp = now.getTime();
                        logComponents[logResponse.serverId]({
                            key: `ERROR-MSG-${Math.random()}`,
                            timeStamp: timeStamp,
                            time: now.toLocaleString(),
                            level: 16,
                            message: `错误代码：${logResponse.status}, 错误消息：${logResponse.msg}`
                        })
                    }
                } catch (e) {
                    console.error(e);
                    notification.warning({
                        message: '解析日志失败',
                        description: `${e.message}`,
                    });
                }
            });

        } catch (e) {
            console.error(e);
            notification.warning({
                message: '建立连接失败',
                description: `${e.message}`,
            });
        }
    }
}


