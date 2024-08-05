import {Breadcrumb} from 'antd';
import React from 'react';

export default function AppBreadcrumb({style, currNav}) {
    return <Breadcrumb style={style}
                       items={currNav.map(item => ({title: item.label}))}
    />;
}