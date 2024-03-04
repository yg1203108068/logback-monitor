export interface TreeNode<U> {
    // 节点数据
    nodeData: U,
    // 根节点
    rootNode: boolean,
    // 叶子节点
    leafNode: boolean,
    // 父节点
    parentNode: any,
    // 深度
    depth: number,
    // 子节点
    childrenNode: TreeNode[],
    // 兄弟节点
    brotherNode: TreeNode[]

}

/**
 * 递归处理器,用于处理菜单
 * @param objElement 对象元素
 * @param expectAttrNames 期望在对象中获取的属性 [{key: 属性名称; call:对该属性进行的处理}]对每个需要的属性进行处理，不在该参数中的属性不会返回
 * @param filter 过滤器，处理过程中对树枝和叶子进行过滤
 * @return 处理后树结构的结果
 *  eg: 将 label 的值转换为 Link 对象
 *  objElement = {
 *      label:"",
 *      key:"",
 *      link:"",
 *      children:
 *      [ {label:"",key:"",link:""} ]
 *  }
 *   转换后
 *  result = {
 *      label:<Link></Link>,
 *      key:"",
 *      children:[ {label:<Link></Link>,key:"",link:""} ]
 *  }
 */
export function recursiveHandler(objElement: { children: {} }, expectAttrNames: { key: string, call: (value, objElement)=>any }[], filter: objElement=>boolean) {
    if (filter !== undefined && filter(objElement) === false) {
        return
    }
    let result = {}
    expectAttrNames.forEach(item => {
        if (item.call !== undefined) {
            result[item.key] = item.call(objElement[item.key], objElement)
        } else {
            result[item.key] = objElement[item.key]
        }
    })
    if (objElement.children !== undefined && objElement.children !== {}) {
        let childrenSub = []
        objElement.children.forEach(item => {
            childrenSub.push(recursiveHandler(item, expectAttrNames))
        })
        result.children = childrenSub
    }
    return result;
}

/**
 * 将树结构数据改成扁平化树结构
 * @param treeData
 * @param rootNode
 * @param parentNode
 * @returns {TreeNode[]}
 */
export function toFlatTree<U>(treeData: [], rootNode = true, parentNode: TreeNode = null, depth: number = 0): TreeNode<U>[] {
    let result: TreeNode<U>[] = [];
    treeData.forEach(treeDataItem => {
        let leafNode = treeDataItem.children === undefined || treeDataItem.children === [] || treeDataItem.children.length == 0;
        let treeNode = {
            nodeData: treeDataItem,
            // 根节点
            rootNode,
            // 叶子节点
            leafNode,
            // 深度
            depth,
            // 父节点
            parentNode: rootNode === true ? null : parentNode,
        }
        // 子节点
        treeNode.childrenNode = leafNode === true ? [] : toFlatTree(treeDataItem.children, false, treeNode, depth + 1)
        if (leafNode === false) {
            treeNode.childrenNode.forEach(item => result.push(item))
        }
        // 兄弟节点
        treeNode.brotherNode = result
        result.push(treeNode);
    })
    return result;
}

/**
 * 从树形结构找 parent 并获取指定字段
 */
export function getAllAttrOfAllDepth(treeNode: TreeNode, attr: string | string[]): string[] {
    if (typeof (attr) === "string" || attr.length == 1) {
        return treeNode.rootNode === true ? [treeNode[attr]] : [...getAllAttrOfAllDepth(treeNode.parentNode, attr), treeNode[attr]]
    }
    let value;
    for (let i = 0; i < attr.length; i++) {
        if (i == 0) {
            value = treeNode[attr[0]];
        } else {
            value = value[attr[i]];
        }
    }
    console.log(value)
    return treeNode.rootNode === true ? [value] : [...getAllAttrOfAllDepth(treeNode.parentNode, attr), treeNode[attr]]
}