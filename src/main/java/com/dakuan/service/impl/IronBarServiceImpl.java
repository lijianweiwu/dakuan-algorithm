package com.dakuan.service.impl;


import com.dakuan.common.Result;
import com.dakuan.domian.IronBarItem;
import com.dakuan.domian.OptimizationItem;
import com.dakuan.domian.OrderEntity;
import com.dakuan.domian.response.IronBarVO;
import com.dakuan.service.IronBarBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IronBarServiceImpl implements IronBarBaseService {
    @Override
    public Result<IronBarVO> optimizate(List<IronBarItem> kc, List<IronBarItem> yl, List<OrderEntity> od, OptimizationItem oi) {
        // 1.把库存封装为map集合
        // 封装kcMap方法
        Map<String, Map<BigDecimal, Map<BigDecimal, IronBarItem>>> kcMap = initKcMap(kc);
        // 2.把订单拆分为单个实体类
        //订单需求钢筋list
        List<IronBarItem> orderlist = new ArrayList<>();
        //订单需求钢筋规格与直径拼接
        Set<String> typeDiamSet = new HashSet();
        for (OrderEntity orderEntity : od) {
            List<IronBarItem> ironBarItems = orderEntity.getIronBarItems();
            // 拆分订单方法
            splitIronBar(ironBarItems, orderlist, typeDiamSet);
        }

        // 3.为返回准备的对象
        IronBarVO vo = new IronBarVO();

        // 4.开始优化
        if (oi.isRawMaterial() && !oi.isRemainMaterial()) {//使用库存、不使用余料
            if (!oi.isMixNorm() && !oi.isWeld()) {  //不降格代用 不焊接
                // 4.1.1 按型号规格遍历订单
                for (String typeAndDiam : typeDiamSet) {
                    String type = typeAndDiam.split("@")[0]; // 型号
                    BigDecimal diam = new BigDecimal(typeAndDiam.split("@")[1]); //直径
                    // 该型号直径的订单集合
                    List<IronBarItem> olist = orderlist.stream().filter(o -> diam.equals(o.getDiam()) && type.equals(o.getType())).sorted(Comparator.comparing(IronBarItem::getLength).reversed()).collect(Collectors.toList());
                    // 该型号直径的库存集合
                    selectKcMap(kcMap,type,diam,false);


                }

            }else if(oi.isMixNorm() && !oi.isWeld()){ //降格代用 不焊接

            }
        }
        return null;
    }

    /**
     *
     * @param kcMap
     * @param type
     * @param diam
     * @param b
     */
    private void selectKcMap(Map<String, Map<BigDecimal, Map<BigDecimal, IronBarItem>>> kcMap, String type, BigDecimal diam, boolean b) {

    }

    /**
     * //把订单拆分为单根钢筋实体类
     *
     * @param sourceList
     * @param targetList
     * @param typeDiamSet
     */
    private void splitIronBar(List<IronBarItem> sourceList, List<IronBarItem> targetList, Set<String> typeDiamSet) {
        if (!CollectionUtils.isEmpty(sourceList)) {
            for (IronBarItem item : sourceList) {
                if (item.getNum() > 0) {
                    for (int i = 0; i < item.getNum(); i++) {
                        IronBarItem ironBarItem = new IronBarItem();
                        BeanUtils.copyProperties(item, ironBarItem);
                        ironBarItem.setNum(1);
                        targetList.add(ironBarItem);
                    }
                    typeDiamSet.add(item.getType() + "@" + item.getDiam());
                }
            }
        }
    }

    /**
     * 把库存kc封装成 Map<型号, Map<直径, Map<长度, IronBarItem>>>
     *
     * @param kc
     * @return
     */
    private Map<String, Map<BigDecimal, Map<BigDecimal, IronBarItem>>> initKcMap(List<IronBarItem> kc) {
        Map<String, Map<BigDecimal, Map<BigDecimal, IronBarItem>>> kcMap = new HashMap<>();
        kc.forEach(item -> {
            BigDecimal diam = item.getDiam();
            String diamId = item.getDiamId();
            BigDecimal length = item.getLength();
            String type = item.getType();
            String typeId = item.getTypeId();
            // 防止钢筋数量小于0
            Integer num = item.getNum() < 0 ? 0 : item.getNum();

            if (kcMap.containsKey(type)) {
                // 相同型号库存map
                Map<BigDecimal, Map<BigDecimal, IronBarItem>> commonTypeMap = kcMap.get(type);
                if (commonTypeMap.containsKey(diam)) {
                    // 相同型号相同直径库存map
                    Map<BigDecimal, IronBarItem> commonDiamMap = commonTypeMap.get(diam);
                    if (commonDiamMap.containsKey(length)) {
                        // 相同型号相同直径相同长度钢筋 只修改数量
                        IronBarItem ironBarItem = commonDiamMap.get(length);
                        ironBarItem.setNum(ironBarItem.getNum() + num);
                    } else { //没有相同长度的钢筋就添加
                        commonDiamMap.put(length, item);
                    }
                } else { //没有相同直径就添加
                    Map<BigDecimal, IronBarItem> lenMap = new HashMap<>();
                    lenMap.put(length, item);
                    commonTypeMap.put(diam, lenMap);
                }
            } else { // 没有相同型号钢筋就添加
                Map<BigDecimal, IronBarItem> lenMap = new HashMap<>();
                lenMap.put(length, item);
                Map<BigDecimal, Map<BigDecimal, IronBarItem>> diamMap = new HashMap<>();
                diamMap.put(diam, lenMap);
                kcMap.put(type, diamMap);
            }
        });
        return null;
    }

}
/*

        // 判断钢筋规格是否在国标范围内
        for (String str : orderggSet) {

            Double num = Double.parseDouble(str.split("@")[1]);
            if (!Diam.isCode(num)) {
                return Result.error(str + CommonConstants.DIAM_ERROR);
            }
        }




                    List<IronBarEntity> klist = kclist.stream().filter(k -> ordergg.equals(k.getDiam()) && type.equals(k.getType())).sorted(Comparator.comparing(IronBarEntity::getLength)).collect(Collectors.toList());
                    // 库存该型号最长的钢筋
                    Double maxLength = klist.get(klist.size() - 1).getLength();


                    if (olist != null && !olist.isEmpty() && (klist == null || klist.isEmpty())) {
                        return Result.error(str + CommonConstants.UNDER_STOCK);
                    }
//                    循环订单
                    while (olist.size() > 0) {
                        // 由于计算量太大，选出需要参与全排列的子集
                        List<IronBarEntity> ironBarEntities = selectSunset(olist, maxLength);
                        //所有排列组合
                        List<List<IronBarEntity>> zuheList = arrangeSelect1(ironBarEntities, maxLength);
                        Map<String, Double> rs = new HashMap<>();
                        //循环排列组合的各个方式
                        rs = addMinRs(rs, zuheList, false, ordergg, klist);
                        if (rs.isEmpty()) {
                            return Result.error(str + CommonConstants.UNDER_STOCK);
                        }
                        // 余料最少的key
                        String minkey = getKeyByMinValue(rs);
                        String[] index = minkey.split("-");
                        //取出余料最少的订单集合
                        List<IronBarEntity> odListWc = zuheList.get(Integer.valueOf(index[0]));
                        //取出余料最少库存实体
                        IronBarEntity kEnty = klist.get(Integer.valueOf(index[1]));

                        //组合切割料字符串
                        StringBuffer sb = new StringBuffer("");
                        //组合长度
                        AtomicReference<Double> zh = new AtomicReference<>(0d);

                        odListWc.forEach(item -> {
                            sb.append("-").append(item.getLength());
                            zh.set(zh.get() + item.getLength());
                        });

                        //余料或废料
                        Double yfl = kEnty.getLength() - zh.get();
                        StringBuffer sb2 = new StringBuffer("");
                        sb2.append(kEnty.getType()).append("_").append(kEnty.getDiam()).append("_").append(kEnty.getLength()).append(sb).append("%").append(yfl);
                        // 处理返回结果集，把开单信息添加到结果集中
                        addIronBarResults(ironBarResults, kEnty, sb, sb2, yfl);

                        // 处理循环内部库存
                        if (yfl.equals(0d)) {//余料为0，移除库存钢筋
                            klist.remove(kEnty);
                        } else {// 设置库存钢筋长度
                            kEnty.setLength(yfl);
                        }
                        olist.removeAll(odListWc);
                    }
                }

            } else if (oi.isMixNorm() && !oi.isWeld()) {//&& oi.isMixOrder() 是否混合订单合并，逻辑一致

                for (String str : orderggSet) {
                    String type = str.split("@")[0];
                    Double ordergg = Double.parseDouble(str.split("@")[1]);
                    //过滤出型号大于等于订单的钢筋
                    // 定义订单降序排序集合
                    List<IronBarEntity> olist = orderlist.stream().filter(o -> ordergg.equals(o.getDiam()) && type.equals(o.getType())).sorted(Comparator.comparing(IronBarEntity::getLength).reversed()).collect(Collectors.toList());
                    // 定义库存变量
                    List<IronBarEntity> klist;
                    if (ordergg > 40d) {//钢筋已经是最大型号了
                        klist = kclist.stream().filter(o -> ordergg.equals(o.getDiam()) && type.equals(o.getType())).sorted(Comparator.comparing(IronBarEntity::getLength)).collect(Collectors.toList());
                    } else {// 取出大于订单尺寸一个型号的钢筋
                        Integer dimaId = Diam.getId(ordergg);
                        // 大一尺寸的直径
                        Double dimaNum = Diam.getCode(dimaId + 1);
                        klist = kclist.stream().filter(o -> (ordergg.equals(o.getDiam()) && type.equals(o.getType())) || (dimaNum.equals(o.getDiam()) && type.equals(o.getType()))).sorted(Comparator.comparing(IronBarEntity::getLength)).collect(Collectors.toList());
                    }
                    // 库存该型号最长的钢筋
                    Double maxLength = klist.get(klist.size() - 1).getLength();

                    if (olist != null && !olist.isEmpty() && (klist == null || klist.isEmpty())) {
                        return Result.error(str + CommonConstants.UNDER_STOCK);
                    }

                    while (olist.size() > 0) {
                        // 钢筋从长到短排序，优先处理较长钢筋
//                        Collections.sort(olist, (o1, o2) -> o2.getLength().compareTo(o2.getLength()));
                        //由于计算量太大，每次计算20根钢筋
//                        List<IronBarEntity> ironBarEntities = olist.subList(0, Math.min(15, olist.size()));
                        // 选出需要参与全排列的子集
                        List<IronBarEntity> ironBarEntities = selectSunset(olist, maxLength);

                        //所有排列组合
                        List<List<IronBarEntity>> zuheList = arrangeSelect1(ironBarEntities, maxLength);
                        // 定义map集合存放每次比较的余料长度
                        Map<String, Double> rs = new HashMap<>();
                        // 把库存与订单组合相减，结果放入rs中（只使用同规格）
                        rs = addMinRs(rs, zuheList, true, ordergg, klist);
                        if (rs.isEmpty()) {//如果同规格钢筋不足判断大一型号
                            rs = addMinRs(rs, zuheList, false, ordergg, klist);
                        }
                        if (rs.isEmpty()) {//大一型号钢筋还是不满足就返回库存不足
                            return Result.error(str + CommonConstants.UNDER_STOCK);
                        }
                        // 取出余料最少的key
                        String minkey = getKeyByMinValue(rs);
                        String[] index = minkey.split("-");
                        //取出余料最少的订单组合
                        List<IronBarEntity> odListWc = zuheList.get(Integer.valueOf(index[0]));
                        //取出余料最少库存钢筋
                        IronBarEntity kEnty = klist.get(Integer.valueOf(index[1]));

                        //组合切割料字符串
                        StringBuffer sb = new StringBuffer("");
                        //组合长度
                        AtomicReference<Double> zh = new AtomicReference<>(0d);
                        odListWc.forEach(item -> {
                            sb.append("-").append(item.getLength());
                            zh.set(zh.get() + item.getLength());
                        });

                        UUID id = kEnty.getId();
                        //余料或废料
                        Double yfl = kEnty.getLength() - zh.get();
                        // 拼接dtl描述信息
                        StringBuffer sb2 = new StringBuffer("");
                        sb2.append(kEnty.getType()).append("_").append(kEnty.getDiam()).append("_").append(kEnty.getLength()).append(sb).append("%").append(yfl);
                        // 处理返回结果集，把开单信息添加到结果集中
                        addIronBarResults(ironBarResults, kEnty, sb, sb2, yfl);
                        // 处理循环内部库存
                        if (yfl.equals(0d)) {//余料为0，移除库存钢筋
                            klist.remove(kEnty);
                        } else {// 设置库存钢筋长度
                            kEnty.setLength(yfl);
                        }
                        // 移除循环外库存中被使用的钢筋
                        for (int i = 0; i < kclist.size(); i++) {
                            IronBarEntity ironBarEntity = kclist.get(i);
                            if (ironBarEntity.getId().equals(id)) {
                                if (yfl.equals(0d)) {
                                    kclist.remove(ironBarEntity);
                                } else {
                                    ironBarEntity.setLength(yfl);
                                }
                                break;
                            }
                        }
//                            kclist.add(new IronBarEntity(UUID.randomUUID(),ordergg,rs.get(minkey),2));

                        olist.removeAll(odListWc);
                    }
                }
            }
            //  封装结果集
            sylist = addScrapIronBar(ironBarResults, sylist);
            mergeIronBarResults(ironBarResults, ironBarResultMerges);


            vo.setIronBarResultMerges(ironBarResultMerges);
            vo.setRemainIronBarResults(sylist);
            return Result.success(ResultCode.SUCCESS, vo);
        }
        return Result.error(CommonConstants.PLAN_ERROR);
    }

    *//**
 * 合并开单信息
 *
 * @param ironBarResults
 * @param ironBarResultMerges
 * <p>
 * 处理余废料
 * @param ironBarResults 开单集合
 * @param sylist         返回与废料
 * @return 把开单信息添加到IronBarResults中
 * @param ironBarResults 返回的开单集合
 * @param kEnty          库存中拿出的钢筋
 * @param sb             拼接的订单长度字符串
 * @param sb2            返回的dtl描述
 * @param yfl            余料废料长度
 * <p>
 * 选出参加全排列的订单钢筋
 * @param olist     订单集合
 * @param maxLength
 * @return 把每次库存与订单组合长度相减，放入rs中
 * @param rs       长度的结果
 * @param zuheList 订单长度组合
 * @param isDima   是否只使用订单尺寸
 * @param ordergg  订单尺寸
 * @param klist    库存集合
 * @return 排列组合所有可能存在的子集
 * @param data
 * @param maxLength
 * @return 获取value最小是的key
 * @param map
 * @return 处理余废料
 * @param ironBarResults 开单集合
 * @param sylist         返回与废料
 * @return 把开单信息添加到IronBarResults中
 * @param ironBarResults 返回的开单集合
 * @param kEnty          库存中拿出的钢筋
 * @param sb             拼接的订单长度字符串
 * @param sb2            返回的dtl描述
 * @param yfl            余料废料长度
 * <p>
 * 选出参加全排列的订单钢筋
 * @param olist     订单集合
 * @param maxLength
 * @return 把每次库存与订单组合长度相减，放入rs中
 * @param rs       长度的结果
 * @param zuheList 订单长度组合
 * @param isDima   是否只使用订单尺寸
 * @param ordergg  订单尺寸
 * @param klist    库存集合
 * @return 排列组合所有可能存在的子集
 * @param data
 * @param maxLength
 * @return 获取value最小是的key
 * @param map
 * @return 处理余废料
 * @param ironBarResults 开单集合
 * @param sylist         返回与废料
 * @return 把开单信息添加到IronBarResults中
 * @param ironBarResults 返回的开单集合
 * @param kEnty          库存中拿出的钢筋
 * @param sb             拼接的订单长度字符串
 * @param sb2            返回的dtl描述
 * @param yfl            余料废料长度
 * <p>
 * 选出参加全排列的订单钢筋
 * @param olist     订单集合
 * @param maxLength
 * @return 把每次库存与订单组合长度相减，放入rs中
 * @param rs       长度的结果
 * @param zuheList 订单长度组合
 * @param isDima   是否只使用订单尺寸
 * @param ordergg  订单尺寸
 * @param klist    库存集合
 * @return 排列组合所有可能存在的子集
 * @param data
 * @param maxLength
 * @return 获取value最小是的key
 * @param map
 * @return 处理余废料
 * @param ironBarResults 开单集合
 * @param sylist         返回与废料
 * @return 把开单信息添加到IronBarResults中
 * @param ironBarResults 返回的开单集合
 * @param kEnty          库存中拿出的钢筋
 * @param sb             拼接的订单长度字符串
 * @param sb2            返回的dtl描述
 * @param yfl            余料废料长度
 * <p>
 * 选出参加全排列的订单钢筋
 * @param olist     订单集合
 * @param maxLength
 * @return 把每次库存与订单组合长度相减，放入rs中
 * @param rs       长度的结果
 * @param zuheList 订单长度组合
 * @param isDima   是否只使用订单尺寸
 * @param ordergg  订单尺寸
 * @param klist    库存集合
 * @return 排列组合所有可能存在的子集
 * @param data
 * @param maxLength
 * @return 获取value最小是的key
 * @param map
 * @return
 *//*
    private void mergeIronBarResults(List<IronBarResult> ironBarResults, List<IronBarResultMerge> ironBarResultMerges) {
        for (IronBarResult ironBarResult : ironBarResults) { //遍历开单钢筋
            String typeId = ironBarResult.getTypeId();
            String diamId = ironBarResult.getDiamId();
            String dtl = ironBarResult.getDtl();
            Double diam = ironBarResult.getDiam();
            Double length = ironBarResult.getLength();
            String type = ironBarResult.getType();

            String[] arrs = dtl.split("_")[2].split("%")[0].split("-");
            Double[] lens = new Double[arrs.length];
            for (int i = 0; i < arrs.length; i++) { // 把dtl中拼接的订单长度截出，重排序
                lens[i] = Double.parseDouble(arrs[i]);
            }
            Arrays.sort(lens);
            // 重新拼接dtl
            StringBuffer sb = new StringBuffer("");
            sb.append(dtl.split("_")[0]).append("_").append(dtl.split("_")[1]).append("_");

            for (int i = lens.length - 1; i >= 0; i--) {

                if (i == lens.length - 1) {
                    sb.append(lens[i]).append("_");
                } else if (i == 0) {
                    sb.append(lens[i]);
                } else {
                    sb.append(lens[i]).append("-");
                }
            }
            String newDtl = sb.append("%" + dtl.split("%")[1]).toString();

            // 判断返回开单集合中有没有相同的，有就合并
            boolean flag = false;
            for (IronBarResultMerge ironBarResultMerge : ironBarResultMerges) {
                @NotNull Double diam1 = ironBarResultMerge.getDiam();
                @NotNull String type1 = ironBarResultMerge.getType();
                Integer num = ironBarResultMerge.getNum();
                String dtl1 = ironBarResultMerge.getDtl();
                if (diam.equals(diam1) && type.equals(type1) && newDtl.equals(dtl1)) {
                    ironBarResultMerge.setNum(num + 1);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                IronBarResultMerge ironBarResultMerge = new IronBarResultMerge(typeId, diamId, type, diam, length, 1, newDtl);
                ironBarResultMerges.add(ironBarResultMerge);
            }

        }
    }



    *//**
 * 处理余废料
 *
 * @param ironBarResults 开单集合
 * @param sylist         返回与废料
 * @return
 *//*
    private List<IronBarItem> addScrapIronBar(List<IronBarResult> ironBarResults, List<IronBarItem> sylist) {
        // 处理余废料
        for (int i = 0; i < ironBarResults.size(); i++) {
            IronBarResult ironBarResult = ironBarResults.get(i);
            String diamId = ironBarResult.getDiamId();
            String typeId = ironBarResult.getTypeId();
            Double diam = ironBarResult.getDiam();
            String type = ironBarResult.getType();
            String dtl = ironBarResult.getDtl();
            String s = dtl.split("%")[1];
            Double len = Double.parseDouble(s);
            if (!len.equals(0d)) { // 如果有余料
                boolean flag = false;
                for (int j = 0; j < sylist.size(); j++) {

                    IronBarItem ironBarItem = sylist.get(j);
                    if (ironBarItem.getLength().equals(len) && ironBarItem.getDiam().equals(diam)) {
                        ironBarItem.setNum(ironBarItem.getNum() + 1);
                        flag = true;
                        break;
                    }

                }
                if (!flag) {
                    IronBarItem ironBarItem = new IronBarItem(typeId, diamId, type, diam, len, 1);
                    sylist.add(ironBarItem);
                }
            }
        }
        return sylist;
    }

    *//**
 * 把开单信息添加到IronBarResults中
 *
 * @param ironBarResults 返回的开单集合
 * @param kEnty          库存中拿出的钢筋
 * @param sb             拼接的订单长度字符串
 * @param sb2            返回的dtl描述
 * @param yfl            余料废料长度
 *//*
    private void addIronBarResults(List<IronBarResult> ironBarResults, IronBarEntity kEnty, StringBuffer sb, StringBuffer sb2, Double yfl) {
        // 记录返回的优化集合中有没有使用过的钢筋
        AtomicInteger atomicInteger = new AtomicInteger();
        // 获得单根新料实体，
        for (int i = 0; i < ironBarResults.size(); i++) {
            IronBarResult ironBarResult = ironBarResults.get(i);
            if (kEnty.getId().equals(ironBarResult.getId())) {//处理分段计算同一根钢筋
                String dtl = ironBarResult.getDtl();
                StringBuffer sb3 = new StringBuffer(dtl.split("%")[0]);
                sb3.append(sb).append("%").append(yfl);
                ironBarResult.setDtl(sb3.toString());
                atomicInteger.incrementAndGet();
            }
        }
        // 如果没使用过，就新建
        if (atomicInteger.get() == 0) {
            IronBarResult ironBarResultNew = new IronBarResult();
            ironBarResultNew.setTypeId(kEnty.getTypeId())
                    .setDiamId(kEnty.getDiamId())
                    .setDiam(kEnty.getDiam())
                    .setLength(kEnty.getLength())
                    .setId(kEnty.getId())
                    .setType(kEnty.getType())
                    .setDtl(sb2.toString());
            ironBarResults.add(ironBarResultNew);
        }
    }

    *//**
 * 选出参加全排列的订单钢筋
 *
 * @param olist     订单集合
 * @param maxLength
 * @return
 *//*
    private List<IronBarEntity> selectSunset(List<IronBarEntity> olist, Double maxLength) {
        List<IronBarEntity> ironBarEntities = new ArrayList<>();
        for (int i = 0; i < olist.size(); i++) {
            Double length = olist.get(i).getLength();
            // 最多允许该长度钢筋条数
            int maxNum = (int) Math.floor(maxLength / length);
            int num = 0;
            for (int j = 0; j < ironBarEntities.size(); j++) {
                if (length.equals(ironBarEntities.get(j).getLength())) {
                    num++;
                }
            }
            if (num < maxNum) {
                ironBarEntities.add(olist.get(i));
            }
            if (ironBarEntities.size() > 15) {
                break;
            }
        }
        return ironBarEntities;
    }

    *//**
 * 把每次库存与订单组合长度相减，放入rs中
 *
 * @param rs       长度的结果
 * @param zuheList 订单长度组合
 * @param isDima   是否只使用订单尺寸
 * @param ordergg  订单尺寸
 * @param klist    库存集合
 * @return
 *//*
    private Map<String, Double> addMinRs(Map<String, Double> rs, List<List<IronBarEntity>> zuheList, boolean isDima, Double ordergg, List<IronBarEntity> klist) {
        outer:
        for (int i = 0; i < zuheList.size(); i++) {

            List<IronBarEntity> etList = zuheList.get(i);

            // 组合的总长度
            AtomicReference<Double> db = new AtomicReference<>(0d);
            etList.forEach(e -> {
                db.set(db.get() + e.getLength());
            });
            // 遍历库存中钢筋
            for (int j = 0; j < klist.size(); j++) {
                Double diam = klist.get(j).getDiam();
                Double remainLen = klist.get(j).getLength() - db.get();
                if (isDima) {// 只选同规格的
                    if (remainLen >= 0d && ordergg.equals(diam)) {//规格相等且剩余长度大于0
                        rs.put(i + "-" + j, remainLen);
                        if (remainLen.equals(0d)) {
                            break outer;
                        }
                    }
                } else {
                    if (remainLen >= 0d) {//规格相等且剩余长度大于0
                        rs.put(i + "-" + j, remainLen);
                        if (remainLen.equals(0d)) {
                            break outer;
                        }
                    }
                }
            }
        }
        return rs;
    }

    *//**
 * 排列组合所有可能存在的子集
 *
 * @param data
 * @param maxLength
 * @return
 *//*
    public List<List<IronBarEntity>> arrangeSelect1(List<IronBarEntity> data, Double maxLength) {
        int nCnt = data.size();
        //int nBit1 = (0xFFFFFFFF >>> (32 - nCnt));
        int nBit = (int) (Math.pow(2, nCnt) - 1);
        List<List<IronBarEntity>> arrangeAllSet = new ArrayList<>();
        for (int i = 1; i <= nBit; i++) {
            List<IronBarEntity> arrangeSet = new ArrayList<>();

            Double lengthTol = 0d;
            for (int j = 0; j < nCnt; j++) {
                if ((i << (31 - j)) >> 31 == -1) {
                    arrangeSet.add(data.get(j));
                    lengthTol = lengthTol + data.get(j).getLength();
                }
            }
            if (lengthTol <= maxLength) {
                arrangeAllSet.add(arrangeSet);
            }
        }
        return arrangeAllSet;
    }

    *//**
 * 获取value最小是的key
 *
 * @param map
 * @return
 *//*
    public String getKeyByMinValue(Map<String, Double> map) {
        List<Map.Entry<String, Double>> list = new ArrayList(map.entrySet());
        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        return list.get(0).getKey();
    }

    //    list拆分20一组
    public <T> List<List<T>> pasListList(List<T> list) {
        List<List<T>> listlist = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            int i = list.size() / 20 + 1;
            for (int j = 0; j < i; j++) {
                List<T> listSon = new ArrayList<>();
                for (int h = 0; h < 20; h++) {
                    if ((20 * j + h) < list.size()) {
                        listSon.add(list.get(20 * j + h));
                    }
                }
                listlist.add(listSon);
            }
        }
        return listlist;
    }

    public IronBarResult getIronBarResultById(List<IronBarResult> ironBarResults, UUID id) {
        for (IronBarResult ironBarResult2 : ironBarResults) {
            if (ironBarResult2.getId().equals(id)) {
                return ironBarResult2;
            }
        }
        return null;
    }
*/

