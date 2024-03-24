import React, {CSSProperties} from "react";
import {ProTable} from "@ant-design/pro-components";
import {Button, Input, Modal, Result, Space} from "antd";
import {BaseButtonProps, ButtonType} from "antd/lib/button/button";
import {ModalProps} from "antd/lib/modal/Modal";
import {SearchOutlined} from "@ant-design/icons";
import '../css/simple-table-setting.css'

/**
 * 简单表格属性
 */
type SimpleTableProps = {
    tableKey: string,
    data: any,
    columns: any[],
    defaultShowColumns?: any[],
    rowId: string,
    title?: string,
    pagination?: {
        current: number,
        defaultPageSize: number,
        total: number,
        onChange: (page: number, pageSize: number)=>void,
        onShowSizeChange: (current: number, size: number)=>void
    }
}

/**
 * 简单的表格组件
 */
export class SimpleTable extends React.Component<SimpleTableProps> {
    constructor({columns, defaultShowColumns}) {
        super();
        let defaultNotShowColumns = {};
        if (defaultShowColumns !== undefined) {
            columns.forEach(item => {
                if (defaultShowColumns.indexOf(item.key) === -1) {
                    defaultNotShowColumns[item.key] = {show: false};
                }
            })
        } else {
            columns.forEach(item => {
                defaultNotShowColumns[item.key] = {show: true};
            })
        }

        this.state.columnStatus = defaultNotShowColumns;
    }

    state = {
        columnStatus: {}
    }
    setShowColumns = (selectedColumn) => {
        this.setState({
            columnStatus: selectedColumn
        })
    }

    render() {
        const {columnStatus} = this.state;
        return <ProTable
            cardProps={{
                bodyStyle: {paddingInline: "0px"}
            }}
            key={this.props.tableKey}
            dataSource={this.props.data}
            rowKey={item => item[this.props.rowId]}
            columns={this.props.columns}
            columnsState={{
                value: columnStatus,
                onChange: this.setShowColumns,
            }}
            search={false}
            options={{fullScreen: false, reload: false, setting: true}}
            dateFormatter="string"
            headerTitle={this.props.title}
            scroll={{x: "auto"}}
            bordered={true}
        />;
    }
}

/**
 * 加载数据失败的结果
 * @param data{FetchErrorStatus} 加载错误的失败
 * @returns {JSX.Element} 失败结果
 * @constructor
 */
export function FetchDataResult({data}) {
    return <Result
        status="500"
        title={data.code}
        subTitle={data.msg}
    />;
}

/**
 * 一个带按钮的Modal属性
 */
export interface AutoModalProps extends ModalProps {
    showBtnText: string,
    showBtnProps?: BaseButtonProps,
    showBtnStyle?: CSSProperties | undefined,
    type?: ButtonType,
}
/**
 * 一个带按钮的Modal
 */
export class AutoModal extends React.Component<AutoModalProps> {
    state = {
        isModalOpen: false
    }

    showModal = () => {
        this.setIsModalOpen(true);
    };

    setIsModalOpen = (isModalOpen) => {
        this.setState({isModalOpen})
    }

    handleCancel = (e) => {
        this.setIsModalOpen(false);
        if (this.props.onCancel !== undefined) {
            this.props.onCancel(e);
        }
    };

    render() {
        const {isModalOpen} = this.state;
        console.log("保存按钮加载状态", this.props.confirmLoading)
        return ([<Button key={"AUTO_MODAL_BUTTON"}  {...this.props.showBtnProps} onClick={this.showModal} type={this.props.type}
                         style={this.props.showBtnStyle}>{this.props.showBtnText ?? "操作"}</Button>,
            <Modal key={"AUTO_MODAL_MODAL"}
                   {...this.props}
                   open={isModalOpen}
                   onOk={() => {
                       this.props.onOk(this.handleCancel)
                   }}
                   onCancel={this.handleCancel}
                   confirmLoading={this.props.confirmLoading}
                   destroyOnClose
            >
                {this.props.children}
            </Modal>]);
    }
}

/**
 * 通过参数创建支持搜索的列
 * @param dataIndex 目前没用
 * @param label 搜索输入框需要提示的文字
 * @param searchInputRef 需要一个引用对象，可以通过 React.createRef() 在组件内创建一个引用对象
 * @returns {{filterDropdown: (function({setSelectedKeys: *, selectedKeys: *, confirm: *, clearFilters: *}): *), filterIcon: (function(boolean): *), onFilterDropdownOpenChange: *}}
 */
export function genColumnSearchProps(dataIndex, label, searchInputRef) {
    return ({
        filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
            <div style={{padding: 8}}>
                <Input
                    ref={searchInputRef}
                    placeholder={`请输入 ${label}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => {
                        confirm();
                        searchInputRef.current.value = selectedKeys[0];
                    }}
                    style={{marginBottom: 8, display: 'block'}}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => {
                            confirm();
                            searchInputRef.current.value = selectedKeys[0];
                        }}
                        icon={<SearchOutlined/>}
                        size="small"
                        style={{width: 90}}
                    >
                        搜索
                    </Button>
                    <Button
                        onClick={() => {
                            clearFilters();
                            searchInputRef.current.value = '';
                            confirm();
                        }}
                        size="small"
                        style={{width: 90}}
                    >
                        重置
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered: boolean) => (
            <SearchOutlined style={{color: filtered ? '#1890ff' : undefined}}/>
        ),
        // onFilter: (value, record) => String(record[dataIndex]).indexOf(value.trim()) !== -1,
        onFilterDropdownOpenChange: visible => {
            if (visible) {
                setTimeout(() => searchInputRef.current?.select(), 100);
            }
        },
    });
}