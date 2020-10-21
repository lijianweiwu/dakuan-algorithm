package com.dakuan.domian;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
钢筋规格实体
 */

@Data
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("规格及数量")
public class IronBarItem{

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
    @NotNull
    @ApiModelProperty(value = "数量")
    private Integer num;

}
