import React, {Component} from "react";
import loginBackgroundImage from "../../img/login_b.jpg"
import {Button, Card, Form, Input, message, Typography} from "antd"
import {LockOutlined, UserOutlined} from "@ant-design/icons"
import {axiosUtils} from "../../utils/axiosUtils";
import {JSEncrypt} from "jsencrypt";
import Cookies from 'js-cookie';
import {LoginSign} from "../../constant/commonEnum";

const {Title} = Typography;

export default class Login extends Component {

    login = (values, publicKey) => {
        let encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        values.password = encrypt.encrypt(values.password);
        axiosUtils.post(`/api/login`, values, data => {
            console.log(data)
            message.success("登录成功");
            this.props.loginSuccess(data.token);
            Cookies.set(LoginSign.token, data.token, {expires: 1, path: '/'});
        }, (code, msg) => {
            if (code === 500 || code === 401) {
                message.error(msg);
            } else {
                message.error("系统异常");
            }
        })
    }

    onFinish = (values) => {
        axiosUtils.get(`/api/username/${values.username}`, data => {
            this.login(values, data)
        }, (code, msg) => {
            if (code === 500) {
                message.error(msg);
            } else {
                message.error("系统异常");
            }
        })
    }

    render() {
        return (
            <div style={{width: "100%", height: "100%", backgroundColor: "#CCC", backgroundSize: "100% 100%", backgroundImage: `url(${loginBackgroundImage})`}}>
                <Card style={{width: 300, position: "absolute", top: "25%", right: "5%"}}>
                    <Title level={2}>查询系统</Title>
                    <Form
                        name="normal_login"
                        className="login-form"
                        initialValues={{remember: true}}
                        onFinish={this.onFinish}
                    >
                        <Form.Item
                            name="username"
                            rules={[{required: true, message: 'Please input your Username!'}]}
                        >
                            <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Username"/>
                        </Form.Item>
                        <Form.Item
                            name="password"
                            rules={[{required: true, message: 'Please input your Password!'}]}
                        >
                            <Input
                                prefix={<LockOutlined className="site-form-item-icon"/>}
                                type="password"
                                placeholder="Password"
                            />
                        </Form.Item>
                        <Form.Item>
                            <Button type="primary" htmlType="submit" className="login-form-button">
                                登录
                            </Button>
                        </Form.Item>
                    </Form>
                </Card>
            </div>
        );
    }
}