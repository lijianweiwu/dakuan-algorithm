package com.dakuan.service;



import com.dakuan.common.Result;
import com.dakuan.domian.IronBarItem;
import com.dakuan.domian.OptimizationItem;
import com.dakuan.domian.OrderEntity;
import com.dakuan.domian.response.IronBarVO;

import java.util.List;

/**
 * 判断库存是否足够
 */
public interface IronBarBaseService {

    Result<IronBarVO> optimizate(List<IronBarItem> kc, List<IronBarItem> yl, List<OrderEntity> od, OptimizationItem oi);
}
