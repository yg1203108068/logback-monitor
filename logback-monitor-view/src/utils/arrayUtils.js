/**
 * 对比两个数组,取并集
 * @param originArray {}[] 源对象数组 如：[{id:0,name:"张三"},{id:1,name:"李四"},{id:2,name:"王五"}]
 * @param targetArray 需要从源数组中获取的所有项目的值 如：[0,1]
 * @param indexKey 根据源对象数组中需要对比的键,如：id
 * 上面示例，会在源数组对象中找到 id = 0 和 id = 1 的
 */
export function contrastGetUnionSet(originArray, targetArray, indexKey) {
    let result = [];
    originArray.forEach(item => {
        if (targetArray.indexOf(item[indexKey]) != -1) {
            result.push(item);
        }
    });
    return result;
}
