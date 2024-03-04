import React from "react";
import ReactDOM from "react-dom";
import Application from "./App";
import 'moment/locale/zh-cn'
import zhCN from 'antd/locale/zh_CN';
import {ConfigProvider} from "antd";

ReactDOM.render(<ConfigProvider locale={zhCN}><Application/></ConfigProvider>, document.getElementById('root'));
