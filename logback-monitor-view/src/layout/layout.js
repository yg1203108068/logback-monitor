import React, {Component} from 'react';
import {Layout} from 'antd';
import {navTreeNodeArr} from "../constant/navMenu";
import {getAllAttrOfAllDepth} from "../utils/treeUtils";
import AppLeftNav from "./appLeftNav";
import AppHeader from "./header";
import AppBreadcrumb from "./breadcrumb";
import {Route, Routes} from "react-router-dom";

const {Header, Content, Sider} = Layout;
export default class AppLayout extends Component {
    setCollapsed = (value) => {
        this.setState({collapsed: value})
    }
    //完整路径的导航对象数组
    currFullNav

    constructor() {
        super();
        let currNode = navTreeNodeArr.find(item => {
            return item.nodeData.link === window.location.pathname
        })
        if (currNode !== undefined) {
            this.currFullNav = getAllAttrOfAllDepth(currNode, "nodeData");
        } else {
            this.currFullNav = [navTreeNodeArr[0]];
        }
    }

    state = {
        collapsed: false,
        appBreadcrumbKey: "",
        switchEnv: "success",
        checkEnvBtnLoading: false,
        percent: 0
    }


    render() {
        const {appBreadcrumbKey} = this.state
        return (
            <Layout style={{height: "100%"}}>
                {this.mainLayout(appBreadcrumbKey)}
            </Layout>
        );
    }

    changeNav = (menuItem) => {

        console.log("菜单发生更改", menuItem.key)
        let currMenuItemKey = menuItem.key.substr("MENU_ITEM_".length, menuItem.key.length)
        let currNode = navTreeNodeArr.find(item => {
            return item.nodeData.key === currMenuItemKey;
        })
        console.log("菜单发生更改当前节点", currNode)
        this.currFullNav = getAllAttrOfAllDepth(currNode, "nodeData");

        this.setState({
            appBreadcrumbKey: currMenuItemKey
        })

    }

    /**
     * 主界面布局
     * @param appBreadcrumbKey
     * @returns {JSX.Element}
     * @constructor
     */
    mainLayout = (appBreadcrumbKey) => {
        const {collapsed} = this.state
        return <Layout>
            <Sider
                collapsible={true}
                collapsed={collapsed}
                onCollapse={this.setCollapsed}
                theme={"light"}
                className={"custom-scrollbar-container"}
                style={{height: '100vh', left: 0, top: 0, bottom: 0, overflowY: "auto"}}>
                <AppLeftNav currFullNav={this.currFullNav} onChange={this.changeNav}/>
            </Sider>
            <Layout style={{height: "100%"}}>
                <Header style={{backgroundColor: "#fff", lineHeight: "45px", height: "45px"}}>
                    <AppHeader/>
                </Header>
                <AppBreadcrumb key={appBreadcrumbKey} style={{margin: '16px 16px'}} currNav={this.currFullNav}/>
                <Content style={{margin: '0 16px 16px', height: "100%",}}>
                    <div style={{backgroundColor: "#fff", padding: 24, height: "100%", overflowX: "hidden"}}
                         className={"custom-scrollbar-container"}>
                        <Routes>
                            {navTreeNodeArr.map(item =>
                                <Route key={"ROUTE_" + item.nodeData.key}
                                       path={item.nodeData.link}
                                       element={item.nodeData.component}/>)
                            }
                        </Routes>
                    </div>
                </Content>
            </Layout>
        </Layout>;
    }
}