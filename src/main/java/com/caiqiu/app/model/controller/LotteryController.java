package com.caiqiu.app.model.controller;


import com.caiqiu.app.utils.Result;
import com.caiqiu.app.vo.lottery.LotteryVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
@Api(value = "开奖业务",tags = "开奖业务",description = "开奖业务")
@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    /**
     * 获取开奖列表
     *
     * @return
     */
    @Operation(summary = "获取开奖列表")
    @PostMapping("/getLottery")
    public Result getLottery(@RequestBody LotteryVO lotteryVO) {
        // TODO: 2022/10/11
        return null;
    }
}

