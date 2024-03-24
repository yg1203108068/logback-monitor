import React from "react";
import ReactDOM from "react-dom";
import Application from "./App";
import 'moment/locale/zh-cn'
import zhCN from 'antd/locale/zh_CN';
import {ConfigProvider, theme} from "antd";

ReactDOM.render(
    <ConfigProvider locale={zhCN} theme={window.matchMedia("(prefers-color-scheme: dark)").matches?{algorithm: theme.darkAlgorithm}:{}}>
        <Application/>
    </ConfigProvider>, document.getElementById('root'));
