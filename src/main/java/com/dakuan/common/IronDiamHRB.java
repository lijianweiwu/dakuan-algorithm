package com.dakuan.common;

import java.math.BigDecimal;

/**
 * 钢筋规格字典
 */
public enum IronDiamHRB {
    HRB0(0, new BigDecimal(6)),
    HRB1(1, new BigDecimal(8)),
    HRB2(2, new BigDecimal(10)),
    HRB3(3, new BigDecimal(12)),
    HRB4(4, new BigDecimal(14)),
    HRB5(5, new BigDecimal(16)),
    HRB6(6, new BigDecimal(18)),
    HRB7(7, new BigDecimal(20)),
    HRB8(8, new BigDecimal(22)),
    HRB9(9, new BigDecimal(25)),
    HRB10(10, new BigDecimal(28)),
    HRB11(11, new BigDecimal(32)),
    HRB12(12, new BigDecimal(36)),
    HRB13(13, new BigDecimal(40)),
    HRB14(14, new BigDecimal(50));
    private Integer id;
    private BigDecimal code;

     IronDiamHRB(Integer id, BigDecimal code) {
        this.code = code;
        this.id = id;
    }

    public static BigDecimal getCode(Integer id) {
        IronDiamHRB[] IronDiams = values();
        for (IronDiamHRB IronDiam : IronDiams) {
            if (IronDiam.id.equals(id)) {
                return IronDiam.code();
            }
        }
        return null;
    }

    public static Integer getId(BigDecimal code) {
        IronDiamHRB[] IronDiams = values();
        for (IronDiamHRB IronDiam : IronDiams) {
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
        for (IronDiamHRB d: IronDiamHRB.values()){
            if(d.code().equals(num)||d.code().compareTo(num)==0){
                flag = true;
                break;
            }
        }
        return flag;
    }
    public static boolean isId(Integer id){
        boolean flag = false;
        for (IronDiamHPB d: IronDiamHPB.values()){
            if(d.id().equals(id)){
                flag = true;
                break;
            }
        }
        return flag;
    }

}
