import {Breadcrumb} from 'antd';
import React from 'react';

export default function AppBreadcrumb({style, currNav}) {
    return <Breadcrumb style={style}>
        {currNav.map(item => <Breadcrumb.Item key={"Breadcrumb_" + item.key}>{item.label}</Breadcrumb.Item>)}
    </Breadcrumb>;
}