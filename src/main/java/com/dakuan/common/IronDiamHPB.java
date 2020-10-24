package com.dakuan.common;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 钢筋规格字典
 */
public enum IronDiamHPB {
    HPB0(0, new BigDecimal(5.5)),
    HPB1(1, new BigDecimal(6)),
    HPB2(2, new BigDecimal(6.5)),
    HPB3(3, new BigDecimal(7)),
    HPB4(4, new BigDecimal(8)),
    HPB5(5, new BigDecimal(9)),
    HPB6(6, new BigDecimal(10)),
    HPB7(7, new BigDecimal(11)),
    HPB8(8, new BigDecimal(12)),
    HPB9(9, new BigDecimal(13)),
    HPB10(10, new BigDecimal(14)),
    HPB11(11, new BigDecimal(15)),
    HPB12(12, new BigDecimal(16)),
    HPB13(13, new BigDecimal(17)),
    HPB14(14, new BigDecimal(18)),
    HPB15(15, new BigDecimal(19));
    private Integer id;
    private BigDecimal code;

     IronDiamHPB(Integer id, BigDecimal code) {
        this.code = code;
        this.id = id;
    }

    public static BigDecimal getCode(Integer id) {
        IronDiamHPB[] ironDiamHPBS = values();
        for (IronDiamHPB IronDiamHPB : ironDiamHPBS) {
            if (IronDiamHPB.id.equals(id)) {
                return IronDiamHPB.code();
            }
        }
        return null;
    }

    public static Integer getId(BigDecimal code) {
        IronDiamHPB[] ironDiamHPBS = values();
        for (IronDiamHPB IronDiamHPB : ironDiamHPBS) {
            if (IronDiamHPB.code().equals(code)) {
                return IronDiamHPB.id();
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
        for (IronDiamHPB d: IronDiamHPB.values()){
            if(d.code().equals(num)||d.code().compareTo(num)==0){
                flag = true;
                break;
            }
        }
        return flag;
    }
    public static boolean isId(Integer num){
        boolean flag = false;
        for (IronDiamHPB d: IronDiamHPB.values()){
            if(d.id().equals(num)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        System.out.println(objectObjectHashMap.get("123"));
    }
}
