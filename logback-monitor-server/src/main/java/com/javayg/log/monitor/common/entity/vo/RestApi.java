package com.javayg.log.monitor.common.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Data
@Accessors(chain = true)
public class RestApi<T> {
    private int code;
    private String msg;
    private T data;

    public RestApi() {
    }

    public RestApi(int code) {
        this.code = code;
    }

    public static <T> RestApi<T> success(T data) {
        return new RestApi<T>(200).setData(data);
    }

    public static <T> RestApi<T> fail(String msg) {
        return new RestApi<T>(500).setMsg(msg);
    }

    public static <T> RestApi<T> fail(String msg, int code) {
        return new RestApi<T>(code).setMsg(msg);
    }
}
