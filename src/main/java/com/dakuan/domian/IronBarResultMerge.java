package com.dakuan.domian;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@ApiModel("优化后的开料合并")
public class IronBarResultMerge {
    @NotNull
    @ApiModelProperty(value = "型号id")
    private String typeId;

    @NotNull
    @ApiModelProperty(value = "直径id")
    private String diamId;

    @NotNull
    @ApiModelProperty(value = "型号")
    private String type;

    @NotNull
    @ApiModelProperty(value = "直径")
    private BigDecimal diam;//直径

    @NotNull
    @ApiModelProperty(value = "长度")
    private BigDecimal length;//长度

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "描述字符串（例：12-1000_450_450%50;表示：直径-原材料长_切割长度_切割长度%余料长度）")
    private String dtl;
}
