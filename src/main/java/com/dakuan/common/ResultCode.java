package com.dakuan.common;

/**
 * 枚举了一些常用API操作码
 * Created on 2019/4/19.
 */
public enum ResultCode {
    SUCCESS(200, "订单智能优化成功！"),
    FAILED(5000, "订单智能优化失败,请重新调整方案！"),
    UNDER_STOCK(5001,"库存不足,请重新调整方案！"),
    DIAM_ERROR(5002,"未知型号，暂不支持智能优化,请重新调整方案！"),
    PARAMETER_NULL(5003,"库存或订单为空，请重新调整优化方案！"),
    NOT_SUPPORT(5004,"暂不支持焊接与余料优化，请重新调整优化方案！");


    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
