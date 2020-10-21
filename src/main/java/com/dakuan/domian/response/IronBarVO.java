package com.dakuan.domian.response;

import com.dakuan.domian.IronBarItem;
import com.dakuan.domian.IronBarResultMerge;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString
@Accessors(chain = true)
@ApiModel("算法最终优化方案")
public class IronBarVO {

    @ApiModelProperty(value = "开料集合")
    private List<IronBarResultMerge> ironBarResultMerges;


    @ApiModelProperty(value = "剩余料集合")
    private List<IronBarItem> remainIronBarResults;


}
