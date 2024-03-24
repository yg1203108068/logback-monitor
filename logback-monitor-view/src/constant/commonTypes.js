export type FetchResult<T> = {
    code: number,
    msg: string,
    data: T
}
/**
 * 加载错误
 */
export type FetchErrorStatus = {
    code: number,
    msg: string,
}