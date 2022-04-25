package com.homeworkSystem.handler.exceptionhandler;


import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据
    public CommonResult error(Exception e) {
        e.printStackTrace();
        return CommonResult.error().message("执行了全局异常处理..");
    }

    //特定异常
    @ExceptionHandler(ArithmeticException.class)//算数异常
    @ResponseBody //为了返回数据
    public CommonResult error(ArithmeticException e) {
        e.printStackTrace();
        return CommonResult.error().message("执行了ArithmeticException异常处理..");
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseBody
    public CommonResult error(AuthenticationCredentialsNotFoundException e) {
        e.printStackTrace();
        return CommonResult.error().code(ResultCode.Unauthorized);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public CommonResult error(AuthenticationException e) {
        e.printStackTrace();
        return CommonResult.error().code(ResultCode.VerificationError);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public CommonResult error(AccessDeniedException e) {
        e.printStackTrace();
        return CommonResult.error().code(ResultCode.Forbidden);
    }

    //自定义异常
    @ExceptionHandler(MyException.class)
    @ResponseBody //为了返回数据
    public CommonResult error(MyException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return CommonResult.error().code(e.getCode()).message(e.getMsg());
    }

}
