package com.dakuan.exception;


import com.dakuan.common.ResultCode;

/**
 * 自定义异常类，继承运行时异常，减少代码入侵性
 */
public class CustomException extends RuntimeException {
    private ResultCode resultCode;
    public CustomException(ResultCode resultCode){
        //异常信息为错误代码加异常信息
        super("错误代码:"+resultCode.getCode()+" 错误信息:"+resultCode.getMessage());
        this.resultCode=resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
