package com.atguigu.lease.common.exception;

import com.atguigu.lease.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice   // 全局异常处理
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class) //异常类型Exception.class
    @ResponseBody  // 异常接口返回json数据
    public Result handle(Exception e) {
        e.printStackTrace();// 打印异常信息
        return Result.fail();
    }

    //ctrl+h 查看继承树
    @ExceptionHandler(LeaseException.class)
    @ResponseBody
    public Result handle(LeaseException e) {
        e.printStackTrace();
        return Result.fail(e.getCode(), e.getMessage());
    }
}
