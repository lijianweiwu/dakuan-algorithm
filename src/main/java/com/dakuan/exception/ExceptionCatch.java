package com.dakuan.exception;


import com.dakuan.common.Result;
import com.dakuan.common.ResultCode;
import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常捕获类
 */
@ControllerAdvice //增强控制器注解（与@ExceptionHandler配合捕获制定异常）
public class ExceptionCatch {
    //申明日志
    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);
    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();
    static{
        //在这里加入一些基础的异常类型判断
//        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }
    /**
     * 捕获不可预知异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e){
        //打印日志
        LOGGER.error("catch Exception :{}\r\nexception:",e.getMessage(),e);
        if (EXCEPTIONS == null){
            EXCEPTIONS= builder.build();
        }
        final ResultCode resultCode =EXCEPTIONS.get(e.getClass());
        final Result result;
        if (resultCode !=null){//如果已知异常返回已知代码
            result=new Result(resultCode);
        }else {//不是已知异常，直接返回9999
            return Result.error(ResultCode.FAILED);
        }
        return result;
    }
    /**
     * 自定义异常捕获
     * @param e
     * @return
     */
    //与@ControllerAdvice配合捕获自定义异常
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public Result customException(CustomException e){
        //打印日志
        LOGGER.error("catch Exception :{}\r\nexception:",e.getMessage(),e);
        ResultCode resultCode = e.getResultCode();
        return  Result.error(resultCode);

    }
}
