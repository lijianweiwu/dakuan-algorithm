package com.dakuan.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 前端要求变量名首字母大写
 * @param <T>
 */

public class Result<T> implements Serializable {
    /**
     * 状态码
     */
    @JsonProperty("Code")
    private long Code;
    /**
     * 返回消息
     */
    @JsonProperty("Msg")
    private String Msg;
    /**
     * 是否成功
     */
    @JsonProperty("Bflag")
    private boolean Bflag;
    /**
     * 数据条数
     */
    @JsonProperty("DataCount")
    private int DataCount;
    /**
     * 返回数据
     */
    @JsonProperty("Data")
    private T Data;

    public Result() {
    }

    public Result(T data) {
        this.Data = data;
    }

    public Result(long code, String msg) {
        this.Code = code;
        this.Msg = msg;
    }

    public Result(long code, String msg, T data) {
        this.Code = code;
        this.Msg = msg;
        this.Data = data;
    }

    public Result(long code, String msg, boolean bflag, int dataCount) {
        this.Code = code;
        this.Msg = msg;
        this.Bflag = bflag;
        this.DataCount = dataCount;
    }

    public Result(long code, String msg, boolean bflag, int dataCount, T data) {
        this.Code = code;
        this.Msg = msg;
        this.Bflag = bflag;
        this.DataCount = dataCount;
        this.Data = data;
    }

    public static <T> Result<T> error() {
        return error(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    public static <T> Result<T> error(T data) {
        return error(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), data);
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return error(resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> Result<T> error(String msg) {
        return error(ResultCode.FAILED.getCode(), msg);
    }

    public static <T> Result<T> error(long code, String msg) {
        Result Result = new Result(code, msg, false, 0);
        return Result;
    }

    public static <T> Result<T> error(long code, String msg, T data) {
        Result Result = new Result(code, msg, data);
        return Result;
    }


    public static <T> Result<T> success(T data) {
        return new Result(data);
    }

    public static <T> Result<T> success(ResultCode resultCode, T data) {
        return new Result(resultCode.getCode(), resultCode.getMessage(),true,1, data);
    }

    public static <T> Result<T> success(Long code, String msg, T data) {
        return new Result(code, msg, data);
    }

    public static <T> Result<T> success() {
        return success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(String msg) {
        return success(null, msg, null);
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result(ResultCode.SUCCESS.getCode(), msg, data);
    }


    @JsonProperty("Code")
    public long getCode() {
        return Code;
    }
    @JsonProperty("Code")
    public void setCode(long code) {
        Code = code;
    }
    @JsonProperty("Msg")
    public String getMsg() {
        return Msg;
    }
    @JsonProperty("Msg")
    public void setMsg(String msg) {
        Msg = msg;
    }
    @JsonProperty("Bflag")
    public boolean isBflag() {
        return Bflag;
    }
    @JsonProperty("Bflag")
    public void setBflag(boolean bflag) {
        Bflag = bflag;
    }
    @JsonProperty("DataCount")
    public int getDataCount() {
        return DataCount;
    }
    @JsonProperty("DataCount")
    public void setDataCount(int dataCount) {
        DataCount = dataCount;
    }
    @JsonProperty("Data")
    public T getData() {
        return Data;
    }
    @JsonProperty("Data")
    public void setData(T data) {
        Data = data;
    }
}
