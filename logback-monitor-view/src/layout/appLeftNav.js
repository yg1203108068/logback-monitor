import {Menu} from 'antd';
import React from 'react';
import {Link} from "react-router-dom";
import {navMenu} from "../constant/navMenu";
import {recursiveHandler} from "../utils/treeUtils";
import {WindowBasicProp} from "../constant/commonEnum";

export default class AppLeftNav extends React.Component<{ currFullNav: any[], onChange: (item: any)=>void }> {

    render() {
        console.log("window[WindowBasicProp].menu={}", window[WindowBasicProp].menu)
        const {currFullNav} = this.props
        let defaultSelectedKeys = "MENU_ITEM_" + currFullNav[currFullNav.length - 1].key;
        let defaultOpenKeys = [];
        if (currFullNav.length > 1) {
            defaultOpenKeys = currFullNav.slice(0, currFullNav.length - 1).map(item => "MENU_ITEM_" + item.key);
        }
        return <Menu
            mode="inline"
            defaultSelectedKeys={defaultSelectedKeys}
            defaultOpenKeys={defaultOpenKeys}
            onSelect={this.props.onChange}
            items={navMenu.map(item => recursiveHandler(
                // 根元素(包含子元素属性)
                item,
                //对当前菜单
                [
                    // 设置菜单 item 的 key
                    {key: "key", call: value => "MENU_ITEM_" + value},
                    // 设置 显示的内容
                    {key: "label", call: (value, objElement) => objElement.children !== undefined ? value : <Link to={objElement.link}>{value}</Link>},
                    // 设置图标
                    {key: "icon"}]
                ,
                // 过滤器，过滤掉不该显示的菜单项
                (objElement: { key: string }) => window[WindowBasicProp].menu.filter(item => item.code == objElement.key).length > 0)
            )}
        />
    }
}