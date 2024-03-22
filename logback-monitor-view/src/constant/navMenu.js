import {ApartmentOutlined, DeploymentUnitOutlined, SearchOutlined, UserOutlined} from '@ant-design/icons'
import {ReactNode} from "react";
import type {TreeNode} from "../utils/treeUtils";
import {toFlatTree} from "../utils/treeUtils";
import UserManager from "../components/user";
import ShowLogger from "../components/log";
import ColonyLog from "../components/colony";

export interface NavProp {
    /**
     * 唯一标识
     */
    key: string,
    /**
     * 显示文本
     */
    label: string,
    /**
     * 显示图标
     */
    icon: ReactNode,
    /**
     * 链接地址
     */
    link: string,
    /**
     * 路由对象
     */
    component: ReactNode,
    /**
     * 子集
     */
    children: NavProp[]
}

export const navMenu: NavProp[] = [
    {
        key: "WITHOUT_DELAY_LOG",
        label: "单体架构",
        icon: <SearchOutlined/>,
        link: "/log",
        component: <ShowLogger key={"WITHOUT_DELAY_LOG"}/>
    }, {
        key: "COLONY_LOG",
        label: "集群架构",
        icon: <ApartmentOutlined />,
        link: "/colony",
        component: <ColonyLog key={"Colony_Log"}/>
    },{
        key: "DISTRIBUTED_LOG",
        label: "分布式架构",
        icon: <DeploymentUnitOutlined />,
        link: "/distributed",
        component: <ColonyLog key={"Colony_Log"}/>
    }, {
        key: "USER_LIST",
        label: "用户管理",
        icon: <UserOutlined/>,
        link: "/user",
        component: <UserManager key={"USER_LIST"}/>
    }
]

export const navTreeNodeArr: TreeNode<NavProp>[] = toFlatTree(navMenu);