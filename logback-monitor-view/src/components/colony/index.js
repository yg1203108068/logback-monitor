import React from "react";
import {axiosUtils} from "../../utils/axiosUtils";
import {Log} from "../log";

type LogWindow={
    serverId : number,
    serverName:string,
    host:string,
    port:number,
    log:Log[]
}
export default class ColonyLog extends React.Component {
    state={

    }
    componentDidMount() {
        axiosUtils.get("/api/colony/list",data => {
            console.log(data)
        });
    }

    render() {
        return <div>集群架构</div>;
    }
}