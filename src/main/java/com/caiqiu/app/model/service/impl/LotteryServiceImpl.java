package com.caiqiu.app.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.caiqiu.app.model.entity.Lottery;
import com.caiqiu.app.model.dao.LotteryMapper;
import com.caiqiu.app.model.service.LotteryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiqiu.app.vo.lottery.LotteryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public IPage<Lottery> findLotterys(IPage<Lottery> page, LotteryVO lotteryVO) {
        QueryWrapper<Lottery> lotteryQueryWrapper = new QueryWrapper<>();
        if (lotteryVO.getStart_issueNumber() >= 0) {
            //开始期
            lotteryQueryWrapper.lambda().gt(Lottery::getIssueNumber, lotteryVO.getStart_issueNumber());
        }
        if (lotteryVO.getEnd_issueNumber() > lotteryVO.getStart_issueNumber()) {
            //结束期
            lotteryQueryWrapper.lambda().le(Lottery::getIssueNumber, lotteryVO.getEnd_issueNumber());
        }
        if (lotteryVO.isAsc()) {
            //升序
            lotteryQueryWrapper.lambda().orderByAsc(Lottery::getDrawDate);
        } else {
            //降序
            lotteryQueryWrapper.lambda().orderByDesc(Lottery::getDrawDate);
        }
        return baseMapper.selectPage(page, lotteryQueryWrapper);
    }
}
