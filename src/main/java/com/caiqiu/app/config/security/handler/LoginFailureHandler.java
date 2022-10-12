package com.caiqiu.app.config.security.handler;

import com.alibaba.fastjson.JSON;

import com.caiqiu.app.config.security.exception.CustomerAuthenticationException;
import com.caiqiu.app.utils.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 用户认证失败处理类 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //设置客户端响应编码格式
        response.setContentType("application/json;charset=UTF-8"); //获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        String message = null;//提示信息
        int code = 500;//错误编码
        //判断异常类型
//        if(exception instanceof AccountExpiredException){
//            message = "账户过期,登录失败!";
//        }
        if(exception instanceof BadCredentialsException){
            message = "用户名或密码错误!";
        }


//        else if(exception instanceof CredentialsExpiredException){
//            message = "密码过期,登录失败!";
//        }
//        else if(exception instanceof DisabledException){
//            message = "账户被禁用,登录失败!";
//
//        }
//        else if(exception instanceof LockedException){
//            message = "账户被锁,登录失败!";
//        }
        else if(exception instanceof InternalAuthenticationServiceException){ //用户被停用
//            message = "账户不存在,登录失败!";
            message = exception.getMessage();
        } else if (exception instanceof CustomerAuthenticationException) {  //获取token过滤器的错误信息

            message = exception.getMessage();
            System.out.println(message+"~~~~~~~~~~~~~");
            code = 600;
        }else{
            message = "登录失败!"; }
//将错误信息转换成JSON
        String result =
                JSON.toJSONString(Result.error().code(code).msg(message));
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    } }