import React, {Component} from 'react';
import {Divider, Layout, theme} from 'antd';
import {navTreeNodeArr} from "../constant/navMenu";
import {getAllAttrOfAllDepth} from "../utils/treeUtils";
import AppLeftNav from "./appLeftNav";
import AppHeader from "./header";
import AppBreadcrumb from "./breadcrumb";
import {Route, Routes} from "react-router-dom";

const {getDesignToken} = theme;

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
        const globalToken = getDesignToken();
        this.state.colorBgContainer = globalToken.colorBgContainer
        this.state.borderRadiusLG = globalToken.borderRadiusLG
    }

    state = {
        collapsed: false,
        appBreadcrumbKey: "",
        switchEnv: "success",
        checkEnvBtnLoading: false,
        percent: 0,
        colorBgContainer: undefined,
        borderRadiusLG: undefined,
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
        const {collapsed, colorBgContainer, borderRadiusLG} = this.state
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

                <Header style={{lineHeight: "45px", height: "45px", background: colorBgContainer}}>
                    <AppHeader/>
                </Header>
                <AppBreadcrumb key={appBreadcrumbKey} style={{margin: '16px 16px'}} currNav={this.currFullNav}/>
                <Divider style={{marginTop:0,marginBottom:0}}/>
                <Content style={{margin: '0 16px 16px', height: "100%", borderRadius: borderRadiusLG}}>
                    <div style={{padding: 24, height: "100%", overflowX: "hidden"}}
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