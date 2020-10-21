package com.dakuan.domian.request;

import com.dakuan.domian.IronBarItem;
import com.dakuan.domian.OptimizationItem;
import com.dakuan.domian.OrderEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 请求实体
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel("算法优化请求对象")
public class IronBarCMD {
    @NotNull
    @ApiModelProperty(value = "库存新料",required = true)
    private List<IronBarItem> kuCun;

    @NotNull
    @ApiModelProperty(value = "余料",required = true)
    private List<IronBarItem> yuLiao;

    @NotNull
    @ApiModelProperty(value = "需求订单",required = true)
    private List<OrderEntity> orders;

    @NotNull
    @ApiModelProperty(value = "优化方案",required = true)
    private OptimizationItem optimizationItem;
}
