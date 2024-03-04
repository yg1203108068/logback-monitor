package com.javayg.log.monitor.common.controller;

import com.javayg.log.monitor.common.entity.vo.RestApi;
import com.javayg.log.monitor.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.SizeLimitExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public RestApi<String> applicationError(final ApplicationException e) {
        e.printStackTrace();
        return RestApi.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public RestApi<String> badRequest(final MethodArgumentTypeMismatchException e) {
        e.printStackTrace();
        return RestApi.fail("请求异常");

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public RestApi<String> badRequest(final BadCredentialsException e) {
        e.printStackTrace();
        return RestApi.fail(e.getMessage());

    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public RestApi<String> uploadFileOutOfMax(final MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return RestApi.fail("文件过大");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SizeLimitExceededException.class)
    @ResponseBody
    public RestApi<String> requestOutOfMax(final SizeLimitExceededException e) {
        e.printStackTrace();
        return RestApi.fail("请求过大");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestApi<String> sysError(final Exception e) {
        e.printStackTrace();
        return RestApi.fail("服务器内部错误");
    }
}