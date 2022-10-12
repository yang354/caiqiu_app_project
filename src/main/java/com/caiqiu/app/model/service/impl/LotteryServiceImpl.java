package com.caiqiu.app.model.service.impl;

import com.caiqiu.app.model.entity.Lottery;
import com.caiqiu.app.model.dao.LotteryMapper;
import com.caiqiu.app.model.service.LotteryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
@Transactional
@Service
public class LotteryServiceImpl extends ServiceImpl<LotteryMapper, Lottery> implements LotteryService {

}
