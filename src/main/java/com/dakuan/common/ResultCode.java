package com.dakuan.common;

/**
 * 枚举了一些常用API操作码
 * Created on 2019/4/19.
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(5000, "操作失败,请稍后再试！"),
    UNDER_STOCK(5001,"规格库存不足,请重新调整方案！"),
    DIAM_ERROR(5002,"规格钢筋不符,数据字典中无该规格钢筋！"),
    PARAMETER_NULL(5003,"库存或订单为空，请重新调整优化方案！") ;


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
