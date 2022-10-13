package com.caiqiu.app.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caiqiu.app.model.entity.Lottery;
import com.caiqiu.app.model.dao.LotteryMapper;
import com.caiqiu.app.model.service.LotteryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiqiu.app.vo.lottery.LotteryQueryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
@Transactional
@Service
public class LotteryServiceImpl extends ServiceImpl<LotteryMapper, Lottery> implements LotteryService {

    @Override
    public IPage<Lottery> findLotterys(IPage<Lottery> page, LotteryQueryVO lotteryQueryVO) {

        QueryWrapper<Lottery> lotteryQueryWrapper = new QueryWrapper<>();
        if (lotteryQueryVO.getStart_issueNumber() >= 0) {
            //开始期
            lotteryQueryWrapper.lambda().gt(Lottery::getIssueNumber, lotteryQueryVO.getStart_issueNumber());
        }
        if (lotteryQueryVO.getEnd_issueNumber() > lotteryQueryVO.getStart_issueNumber()) {
            //结束期
            lotteryQueryWrapper.lambda().le(Lottery::getIssueNumber, lotteryQueryVO.getEnd_issueNumber());
        }
        if (lotteryQueryVO.isAsc()) {
            //升序
            lotteryQueryWrapper.lambda().orderByAsc(Lottery::getIssueNumber);
        } else {
            //降序
            lotteryQueryWrapper.lambda().orderByDesc(Lottery::getIssueNumber);
        }
        return baseMapper.selectPage(page, lotteryQueryWrapper);
    }

    @Override
    public Long checkIssueNumber(Long issueNumber) {
        QueryWrapper<Lottery> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Lottery::getIssueNumber, issueNumber);
        Long count = baseMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public Long checkDrawDate(String drawDate) {
        QueryWrapper<Lottery> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Lottery::getDrawDate, drawDate);
        Long count = baseMapper.selectCount(queryWrapper);
        return count;
    }
}
