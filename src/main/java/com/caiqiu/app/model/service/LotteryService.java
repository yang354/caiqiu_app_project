package com.caiqiu.app.model.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caiqiu.app.model.entity.Lottery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caiqiu.app.vo.lottery.LotteryQueryVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
public interface LotteryService extends IService<Lottery> {

    IPage<Lottery> findLotterys(IPage<Lottery> page, LotteryQueryVO lotteryQueryVO);

    Long checkIssueNumber(Long issueNumber);

    Long checkDrawDate(String drawDate);
}
