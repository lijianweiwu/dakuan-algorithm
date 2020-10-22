package com.dakuan.exception;


import com.dakuan.common.ResultCode;

/**
 * 异常抛出类
 */
public class ExceptionCast {
    /**
     * 用该静态方法抛出异常
     * @param resultCode
     */
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
