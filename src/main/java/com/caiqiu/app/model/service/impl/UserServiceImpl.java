package com.caiqiu.app.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiqiu.app.model.dao.UserMapper;
import com.caiqiu.app.model.entity.User;
import com.caiqiu.app.model.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findUserByUserName(String username) {
        //创建条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(); //用户名
        queryWrapper.eq("username",username);
        //返回查询记录
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public User findUsers() {
        User user = baseMapper.selectList(null).get(0);
        //返回清除密码
        user.setPassword(null);
        return user;
    }

}
