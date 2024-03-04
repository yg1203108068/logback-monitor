import React from "react";
import {LoginSign, LoginStatus, WindowBasicProp} from "./constant/commonEnum";
import PubSub from "pubsub-js";
import Login from "./components/login/login";
import {BrowserRouter} from "react-router-dom";
import AppLayout from "./layout/layout";
import {message, Spin} from "antd";
import {axiosUtils} from "./utils/axiosUtils";

export default class LoginBuffer extends React.Component {
    state = {
        loginStatus: LoginStatus.unvalidated,
    }

    /**
     * 加载菜单，保存到 window 上
     */
    loadMenu = () => {
        axiosUtils.get("/api/menu", data => {
            window[WindowBasicProp].menu = data;
            this.setState({
                loginStatus: LoginStatus.login
            });
        }, (code, msg) => {
            if (code === 401) {
                this.setState({
                    loginStatus: LoginStatus.logout
                });
            } else {
                message.error(`系统异常：${msg}`);
            }
        });
    }

    componentDidMount() {
        this.loadMenu();
        // 监听退出操作
        PubSub.subscribe(LoginSign.LoginStatusChangeEventKey, () => {
            window.localStorage.removeItem(LoginSign.token)
            message.success("已退出")
            this.setState({loginStatus: LoginStatus.logout})
        })

    }

    loginSuccess = (token) => {
        this.loadMenu();
        window.localStorage.setItem(LoginSign.token, token)
    }


    render() {
        const {loginStatus} = this.state;
        if (loginStatus == LoginStatus.unvalidated) {
            return <div style={{width: "100%", height: "100%", paddingTop: "50px", textAlign: "center"}}><Spin tip="正在登录..." size="large"/></div>
        } else if (loginStatus == LoginStatus.logout) {
            return <Login loginSuccess={this.loginSuccess}/>
        } else {
            return <BrowserRouter><AppLayout/></BrowserRouter>
        }
    }
}