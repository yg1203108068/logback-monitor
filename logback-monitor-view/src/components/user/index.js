import React, {Component} from "react";
import {AutoModal, genColumnSearchProps} from "../../constant/commonComponents";
import {Badge, Button, Checkbox, Form, Input, message, Space, Table} from "antd";
import {axiosUtils} from "../../utils/axiosUtils";

export default class UserManager extends Component {
    usernameSearchInput = React.createRef();
    nicknameSearchInput = React.createRef();
    columns = [
        {title: '用户名', dataIndex: "username", key: "username", ...genColumnSearchProps("customerName", "名称", this.usernameSearchInput)},
        {title: '姓名', dataIndex: "nickname", key: "nickname", ...genColumnSearchProps("projectName", "项目名称", this.nicknameSearchInput)},
        {title: '角色', dataIndex: "roleName", key: "roleName"},
        {title: '禁用', dataIndex: "valid", key: "valid", render: valid => valid ? <Badge status="success" text="可用"/> : <Badge status="error" text="禁用"/>},
        {
            title: '管理', dataIndex: "id", key: "manager", render: (id, record) => {
                console.log(id, record)
                return <>
                    <Space wrap>
                        <Button type={"link"} onClick={() => {
                            this.disable(id)
                        }}>{record.valid ? "禁用" : "启用"}</Button>
                        <Button type={"link"} onClick={() => {
                            this.del(id)
                        }}>删除</Button>
                        <Button type={"link"} onClick={() => {
                            this.reset(id)
                        }}>重置密码</Button>
                        <AddUserModal refresh={this.fetch} record={record} formTitle={"修改信息"}/>
                    </Space>
                </>
            }
        },
    ]
    state = {
        data: [],
        pagination: {current: 1, pageSize: 10},
        filters: {username: null, nickname: null},
    }

    fetchOption(tp, id) {
        axiosUtils.get(`/api/user/${tp}/${id}`, (msg) => {
            message.success(msg)
            this.fetch()
        }, (code, msg) => {
            message.error(msg)
        });
    }

    disable = (id) => {
        this.fetchOption("disable", id)
    }
    del = (id) => {
        this.fetchOption("delete", id)
    }
    reset = (id) => {
        this.fetchOption("reset", id)
    }


    componentDidMount() {
        this.fetch()
    }


    fetch = (pagination, filters) => {
        if (pagination === undefined && filters === undefined) {
            pagination = this.state.pagination
            filters = this.state.filters
        }
        console.log("拉取数据", pagination)
        axiosUtils.get(`/api/user/list?page=${pagination.current - 1}&size=${pagination.pageSize}&username=${filters.username !== null ? filters.username : ""}&nickname=${filters.nickname !== null ? filters.nickname : ""}`, data => {
            this.setState({
                data: data.content,
                pagination: {
                    current: data.number + 1,
                    pageSize: data.size,
                    total: data.totalElements
                }
            })
        }, (code, msg) => {
            console.log("信息列表异常", code, msg)
            message.error(msg)
        });
    }

    handleChange = (pagination, filters) => {
        console.log('Various parameters', pagination, filters);
        let username = filters.username == null ? null : filters.username[0];
        let nickname = filters.nickname == null ? null : filters.nickname[0];
        console.log(username, nickname)
        this.fetch(pagination, {username, nickname})
    }

    render() {
        const {data, pagination} = this.state;
        return <>
            <Space style={{marginBottom: "10px"}}>
                <AddUserModal refresh={this.fetch}/>
            </Space>
            <Table rowKey={(r) => r.id} columns={this.columns} pagination={pagination} dataSource={data} onChange={this.handleChange}/>
        </>;

    }
}

function AddUserModal({refresh, record, formTitle}) {

    let closeModal = undefined
    const form = React.createRef()
    const okHandler = (handlerCancel) => {
        form.current.submit();
        closeModal = handlerCancel;
    }
    const onFinishHandler = (values) => {
        console.log(values);
        axiosUtils.post(`/api/user/${record != undefined ? "edit" : "add"}`, {...record, ...values}, () => {
            message.success(record != undefined ? "用户已修改" : "添加成功");
            closeModal();
            refresh();
        }, (code, msg) => {
            message.error(msg);
        });
    }

    return <>
        <AutoModal showBtnText={formTitle !== undefined ? formTitle : "添加用户"} type={formTitle !== undefined ? "link" : "primary"}
                   title={formTitle !== undefined ? formTitle : "添加用户"} onOk={okHandler}>
            <p>创建用户无需输入密码，初始密码123456，请用户自行更改</p>
            <Form labelCol={{span: 4}} ref={form} name={"AddUserForm"} onFinish={onFinishHandler} initialValues={record !== undefined ? record : {}}>
                <Form.Item label={"账号"} name={"username"} rules={[{required: true, message: '请输入用户账号!'}]}>
                    <Input/>
                </Form.Item>
                <Form.Item label={"姓名"} name={"nickname"} rules={[{required: true, message: '请输入用户姓名!'}]}>
                    <Input/>
                </Form.Item>
                <Form.Item label={"角色"} name={"roleId"} rules={[{required: true, message: '请选择角色!'}]}>
                    <Checkbox.Group>
                        <Checkbox value={1}>用户</Checkbox>
                        <Checkbox value={2}>管理员</Checkbox>
                    </Checkbox.Group>
                </Form.Item>
            </Form>
        </AutoModal>
    </>;
}