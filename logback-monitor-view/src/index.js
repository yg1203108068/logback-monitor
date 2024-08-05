import React from "react";
import Application from "./App";
import {createRoot} from 'react-dom/client';
import 'moment/locale/zh-cn'
import zhCN from 'antd/locale/zh_CN';
import {ConfigProvider, theme} from "antd";

const container = document.getElementById('root');
const root = createRoot(container);
root.render(
    <ConfigProvider locale={zhCN} theme={window.matchMedia("(prefers-color-scheme: dark)").matches?{algorithm: theme.darkAlgorithm}:{}}>
        <Application/>
    </ConfigProvider>, document.getElementById('root'));
