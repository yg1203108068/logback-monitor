/**
 * 调用axios的get方法并自动处理程序
 * @param url
 * @param successHandler
 * @param fail
 */
import axios from "axios";
import type {FetchResult} from "../constant/commonTypes";

const onSuccessHandler = (response, successHandler, fail) => {
    let result: FetchResult = response.data;
    if (result.code === 200) {
        if (successHandler !== undefined) {
            successHandler(result.data)
        }
    } else {
        if (fail != undefined) {
            fail(result.code, result.msg);
        }
    }
    return new Promise(resolve => {
        resolve(response)
    });
}
const onFailHandler = (reason, fail) => {
    console.error(reason)
    if (reason.response !== undefined && reason.response.data !== undefined) {
        if (reason.response.data.code !== undefined) {
            fail(reason.response.data.code, reason.response.data.msg)
        } else {
            fail(reason.response.data.status, reason.response.data.error)
        }
    } else {
        fail(reason.code, reason.message)
    }

    return new Promise((_, reject) => {
        reject(reason)
    });
}


const get = (url: string, successHandler: (data: any)=>void, fail: (code: number, msg: string)=>void) => {
    return axios.get(url)
        .then(response => onSuccessHandler(response, successHandler, fail))
        .catch(reason => onFailHandler(reason, fail))
}
const post = (url: string, data: any, successHandler: (data: any)=>void, fail: (code: number, msg: string)=>void) => {
    return axios.post(url, data)
        .then(response => onSuccessHandler(response, successHandler, fail))
        .catch(reason => onFailHandler(reason, fail))
}
export const axiosUtils = {get, post}