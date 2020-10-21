package com.dakuan.domian;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@ToString
@Accessors(chain = true)
@ApiModel("优化方案属性")
public class OptimizationItem {
    @NotNull
    @ApiModelProperty(value = "钢筋原材")
    @JsonFormat
    private boolean rawMaterial;

    @NotNull
    @ApiModelProperty(value = "钢筋余料")
    @JsonFormat
    private boolean remainMaterial;

    @NotNull
    @ApiModelProperty(value = "混合（订单）相加下料")
    @JsonFormat
    private boolean mixOrder;

    @NotNull
    @ApiModelProperty(value = "混合（规格）-降规格代用下料")
    @JsonFormat
    private boolean mixNorm;

    @NotNull
    @ApiModelProperty(value = "余料焊接下料")
    @JsonFormat
    private boolean weld;
}
