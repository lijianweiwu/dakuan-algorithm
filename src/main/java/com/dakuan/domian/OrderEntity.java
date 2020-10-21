package com.dakuan.domian;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
@Accessors(chain = true)
@ApiModel("订单对象")
public class OrderEntity {
    @NotNull
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @NotNull
    @ApiModelProperty(value = "规格及数量")
    private List<IronBarItem> IronBarItems;
}
