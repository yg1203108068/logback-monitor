import NProgress from "nprogress";
import {ApplicationModeEnum, LoginSign} from "../constant/commonEnum";
import PubSub from 'pubsub-js';
import {ApplicationMode} from "./config";

export function requestConfigInterceptor(config) {
    NProgress.start();
    if (config.headers.token === undefined) {
        config.headers = {
            ...config.headers,
            [LoginSign.token]: window.localStorage.getItem(LoginSign.token),
        }
    }
    if (ApplicationMode == ApplicationModeEnum.DEBUG) {
        config.headers = {
            ...config.headers,
            DEBUG: true
        }
    }

    return config;
}

export function requestOnRejected(error) {
    NProgress.done();
    return new Promise((_, reject) => {
        reject(error)
    });
}

export function responseInterceptor(response) {
    NProgress.done();
    if (response?.status === 200) {
        if (response.data?.code === 401) {
            //未登录
            setTimeout(() => {
                PubSub.publish(LoginSign.LoginStatusChangeEventKey, false)
            }, 500)
            return response;
        } else {
            return response;
        }
    }
}

export function responseOnRejected(error) {
    NProgress.done();
    console.log("响应过滤器", error.response?.status)
    if (error.response?.status === 401) {
        setTimeout(() => {
            PubSub.publish(LoginSign.LoginStatusChangeEventKey, false)
        }, 500);
    }
    return new Promise((_, reject) => {
        reject(error)
    });
}