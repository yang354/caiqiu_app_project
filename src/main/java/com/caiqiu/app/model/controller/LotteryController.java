package com.caiqiu.app.model.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caiqiu.app.model.entity.Lottery;
import com.caiqiu.app.model.service.LotteryService;
import com.caiqiu.app.utils.Result;
import com.caiqiu.app.vo.lottery.LotteryVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
@Api(value = "开奖业务", tags = "开奖业务", description = "开奖业务")
@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    @Resource
    LotteryService lotteryService;

    /**
     * 获取开奖列表
     *
     * @return
     */
    @Operation(summary = "获取开奖列表")
    @PostMapping("/getLottery")
    public Result getLottery(@RequestBody LotteryVO lotteryVO) {
        // TODO: 2022/10/11
//        if (lotteryVO.getEnd_issueNumber() >= 100000) {
//            return null;
//        }
        //创建分页对象
        IPage<Lottery> page = new Page<Lottery>(lotteryVO.getPageNo(), lotteryVO.getPageSize());
        //调用分页查询方法
        lotteryService.findLotterys(page, lotteryVO);
        return Result.ok(page);
    }
}

