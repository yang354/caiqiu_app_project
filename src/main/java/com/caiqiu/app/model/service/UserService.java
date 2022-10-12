package com.caiqiu.app.model.service;

import com.caiqiu.app.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户信息 * @param username
     * @return
     */
    User findUserByUserName(String username);
}
