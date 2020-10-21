package com.dakuan.controller;


import com.dakuan.common.Result;
import com.dakuan.common.ResultCode;
import com.dakuan.domian.request.IronBarCMD;
import com.dakuan.domian.response.IronBarVO;
import com.dakuan.service.IronBarBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "算法", tags = "钢筋开料算法")

@RestController
@Slf4j
@CrossOrigin //允许跨域
public class IronBarController {
    @Autowired
    private IronBarBaseService ironBarBaseService;

    /**
     * 钢筋开料算法
     */

    @ApiOperation(value = "钢筋开料算法")
    @PostMapping(value = "/IronOderSuanfa")
    public Result<IronBarVO> labourComDetailsByPartNo(@RequestBody(required = true) @ApiParam(value = "库存、余料、订单", required = true) IronBarCMD cmd) {
        // 1.判断库存或订单是否为空
        if (ObjectUtils.isEmpty(cmd.getOrders())||ObjectUtils.isEmpty(cmd.getKuCun())){
            return Result.error(ResultCode.PARAMETER_NULL);
        }
        // 2.条用具体优化逻辑
        Result<IronBarVO> result = ironBarBaseService.optimizate(cmd.getKuCun(), cmd.getYuLiao(), cmd.getOrders(), cmd.getOptimizationItem());
        return result;
    }
}
