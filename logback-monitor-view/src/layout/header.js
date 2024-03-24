import React from 'react';
import {CloudServerOutlined, LoginOutlined, SettingOutlined, UserOutlined} from "@ant-design/icons";
import {axiosUtils} from "../utils/axiosUtils";
import {Dropdown, Form, Input, message, Modal, Skeleton,Typography} from "antd"
import {LoginSign} from "../constant/commonEnum";
import PubSub from "pubsub-js";
import {JSEncrypt} from "jsencrypt";
import axios from "axios";
import Cookies from 'js-cookie';

const changePasswordKey = "ChangePassword";
const loginOutKey = "LoginOut";

const items = [
    {
        label: '修改密码',
        key: changePasswordKey,
        icon: <SettingOutlined/>,
    },
    {
        label: '退出登录',
        key: loginOutKey,
        icon: <LoginOutlined/>,
    }
]
const formItemLayout = {
    labelCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 5,
        },
    },
    wrapperCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 16,
        },
    },
};


const form = React.createRef();
export default class AppHeader extends React.Component {

    state = {
        loading: true,
        nickname: undefined,
        isModalOpen: false
    }
    handleMenuClick = (e) => {
        if (e.key === changePasswordKey) {
            this.showModal();
        } else {
            this.logout();
        }
    }
    logout = () => {
        axios.post("/api/logout").then(() => {
            setTimeout(() => {
                PubSub.publish(LoginSign.LoginStatusChangeEventKey, false);
                Cookies.remove(LoginSign.token);
            }, 500);
        }).catch(() => {
            message.error(`退出失败`)
        });
    }
    menuProps = {
        items,
        onClick: this.handleMenuClick,
    };
    modalHandleCancel = () => {
        this.setIsModalOpen(false);
    };
    modalHandleOk = () => {
        form.current.submit();
    };
    showModal = () => {
        this.setIsModalOpen(true);
    };
    setIsModalOpen = (isModalOpen) => {
        this.setState({isModalOpen});
    }

    componentDidMount() {
        axiosUtils.get("/api/nickname", data => {
            this.setState({
                loading: false,
                nickname: data
            })
        }, (code, msg) => {
            message.error("系统初始化失败");
            console.log("加载姓名出错：{}，{}", code, msg)
        });

    }

    onFinish = (values) => {
        values.confirm = undefined
        console.log(values)
        this.changePasswordProcess(values)
    }

    changePasswordProcess(values) {
        axiosUtils.get('/api/token', publicKey => {
            this.changePassword(publicKey, values)
        }, (code, msg) => {
            message.error(`修改密码失败：${msg}`);
        })
    }

    changePassword(publicKey, values) {
        let encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        axiosUtils.post('/api/password', {
            oldPassword: encrypt.encrypt(values.oldPassword),
            newPassword: encrypt.encrypt(values.newPassword)
        }, () => {
            this.setIsModalOpen(false);
            message.success("修改成功");
        }, (code, msg) => {
            if (code === 401) {
                message.error(`密码输入错误：${msg}`);
            } else {
                message.error(`修改密码失败：${msg}`);
                console.log("修改密码失败：", code, msg)
            }
        })
    }

    render() {
        const {loading, nickname, isModalOpen} = this.state;
        return (
            <Typography style={{fontSize: "22px"}}>
                <a href="/"><CloudServerOutlined/></a>

                <span style={{paddingLeft: "20px"}}>开发者选项<span style={{fontSize: "16px"}}>&nbsp;V&nbsp;{process.env.APP_VERSION}</span></span>

                <div style={{float: "right"}}>
                    {loading ? <Skeleton.Input style={{marginTop: "7px"}} active={true}/> :
                        <Dropdown.Button style={{marginTop: "7px"}} menu={this.menuProps} placement="bottom" icon={<UserOutlined/>}>
                            你好,{nickname}
                        </Dropdown.Button>
                    }
                </div>
                <Modal title="修改密码" open={isModalOpen} onOk={this.modalHandleOk} onCancel={this.modalHandleCancel} maskClosable={false} destroyOnClose>
                    <Form
                        {...formItemLayout}
                        ref={form}
                        name="ChangePasswordForm"
                        onFinish={this.onFinish}
                        scrollToFirstError
                    >
                        <Form.Item
                            name="oldPassword"
                            label="旧密码"
                            rules={[
                                {
                                    required: true,
                                    message: '请输入旧密码!',
                                },
                            ]}
                            hasFeedback
                        >
                            <Input.Password visibilityToggle={false}/>
                        </Form.Item>
                        <Form.Item
                            name="newPassword"
                            label="新密码"
                            rules={[
                                {
                                    required: true,
                                    message: '请输入你的密码!',
                                },
                            ]}
                            hasFeedback
                        >
                            <Input.Password visibilityToggle={false}/>
                        </Form.Item>

                        <Form.Item
                            name="confirm"
                            label="确认密码"
                            dependencies={['newPassword']}
                            hasFeedback
                            rules={[
                                {
                                    required: true,
                                    message: '请再次输入你的密码!',
                                },
                                ({getFieldValue}) => ({
                                    validator(_, value) {
                                        if (!value || getFieldValue('newPassword') === value) {
                                            return Promise.resolve();
                                        }
                                        return Promise.reject(new Error('两次输入的密码不一致!'));
                                    },
                                }),
                            ]}
                        >
                            <Input.Password visibilityToggle={false}/>
                        </Form.Item>
                    </Form>
                </Modal>
            </Typography>
        );
    }

}