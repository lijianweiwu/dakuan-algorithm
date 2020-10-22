package com.dakuan.domian;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@Accessors(chain = true)
@ApiModel("开单临时实体类")
public class IronBarResult {

    @ApiModelProperty(value = "标识id")
    private UUID id;

    @NotNull
    @ApiModelProperty(value = "型号id")
    private String typeId;

    @NotNull
    @ApiModelProperty(value = "直径id")
    private String diamId;

    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "直径")
    private BigDecimal diam;

    @ApiModelProperty(value = "长度")
    private BigDecimal length;

    @ApiModelProperty(value = "切割后剩余长度")
    private BigDecimal remainLen;

    @ApiModelProperty(value = "已切割的长度")
    private List<BigDecimal> len;
}
