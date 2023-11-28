package org.fffd.l23o6.controller;

import cn.dev33.satoken.exception.NotLoginException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import io.github.lyc8503.spring.starter.incantation.pojo.CommonResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Exception Controller
 */

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    /**
     * 登录授权异常
     */
    @ExceptionHandler(NotLoginException.class)
    public CommonResponse<?> handleAuthorizeException(NotLoginException e) {
        log.error("Not Login Exception", e);
        return CommonResponse.error(CommonErrorType.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 无元素异常
     */
    @ExceptionHandler(NoSuchElementException.class)
    public CommonResponse<?> handleNoSuchElementException(NoSuchElementException e) {
        log.error("No Such Element Exception", e);
        return CommonResponse.error(CommonErrorType.ILLEGAL_ARGUMENTS, "请求的资源不存在");
    }
}
