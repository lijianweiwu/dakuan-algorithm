package com.dakuan.common;

import java.math.BigDecimal;

/**
 * 钢筋规格字典
 */
public enum IronDiam {
    DIMA0(0, new BigDecimal(3)),
    DIMA1(1, new BigDecimal(4)),
    DIMA2(2, new BigDecimal(5)),
    DIMA3(3, new BigDecimal(6)),
    DIMA4(4, new BigDecimal(8)),
    DIMA5(5, new BigDecimal(10)),
    DIMA6(6, new BigDecimal(12)),
    DIMA7(7, new BigDecimal(14)),
    DIMA8(8, new BigDecimal(16)),
    DIMA9(9, new BigDecimal(18)),
    DIMA10(10, new BigDecimal(20)),
    DIMA11(11, new BigDecimal(22)),
    DIMA12(12, new BigDecimal(25)),
    DIMA13(13, new BigDecimal(28)),
    DIMA14(14, new BigDecimal(30)),
    DIMA15(15, new BigDecimal(32)),
    DIMA16(16, new BigDecimal(36)),
    DIMA17(17, new BigDecimal(38)),
    DIMA18(18, new BigDecimal(40)),
    DIMA19(19, new BigDecimal(50));
    private Integer id;
    private BigDecimal code;

     IronDiam(Integer id, BigDecimal code) {
        this.code = code;
        this.id = id;
    }

    public static BigDecimal getCode(Integer id) {
        IronDiam[] IronDiams = values();
        for (IronDiam IronDiam : IronDiams) {
            if (IronDiam.id.equals(id)) {
                return IronDiam.code();
            }
        }
        return null;
    }

    public static Integer getId(BigDecimal code) {
        IronDiam[] IronDiams = values();
        for (IronDiam IronDiam : IronDiams) {
            if (IronDiam.code().equals(code)) {
                return IronDiam.id();
            }
        }
        return null;
    }

    public BigDecimal code() {
        return code;
    }

    public Integer id() {
        return id;
    }

    /**
     * 判断是否包含该尺寸
     * @param num
     * @return
     */
    public static boolean isCode(BigDecimal num){
        boolean flag = false;
        for (IronDiam d: IronDiam.values()){
            if(d.code().equals(num)){
                flag = true;
                break;
            }
        }
        return flag;
    }
}
