package com.caiqiu.app.config.security.service;


import com.caiqiu.app.config.security.context.AuthenticationContextHolder;
import com.caiqiu.app.model.entity.User;
import com.caiqiu.app.model.service.UserService;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;


@Component
public class CustomerUserDetailsService implements UserDetailsService {
    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        //调用根据用户名查询用户信息的方法
        User user = userService.findUserByUserName(username); //如果对象为空，则认证失败
        //System.out.println(user.toString());
        if (user == null) {
            throw new InternalAuthenticationServiceException("登录用户：" + username + " 不存在");
        }
        if (user.getStatus() == 1) {
            throw new DisabledException("对不起，您的账号：" + user.getUsername() + " 已停用");
        }
        return user;
    }





}