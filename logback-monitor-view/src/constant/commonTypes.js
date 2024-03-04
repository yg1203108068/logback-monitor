export type FetchResult<T> = {
    code: number,
    msg: string,
    data: T
}