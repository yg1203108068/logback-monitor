import React, {Component} from "react";
import axios from "axios";
import {requestConfigInterceptor, requestOnRejected, responseInterceptor, responseOnRejected} from "./config/axiosInterceptor";
import LoginBuffer from "./loginBuffer";
import {WindowBasicProp} from "./constant/commonEnum";

axios.interceptors.request.use(requestConfigInterceptor, requestOnRejected)
axios.interceptors.response.use(responseInterceptor, responseOnRejected)

export default class Application extends Component {
    constructor() {
        super();
        window[WindowBasicProp] = {}
    }

    render() {
        return <LoginBuffer/>
    }
}
