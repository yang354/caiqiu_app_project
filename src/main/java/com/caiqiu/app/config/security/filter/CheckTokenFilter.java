package com.caiqiu.app.config.security.filter;



import com.caiqiu.app.config.redis.RedisService;
import com.caiqiu.app.config.security.exception.CustomerAuthenticationException;
import com.caiqiu.app.config.security.handler.LoginFailureHandler;
import com.caiqiu.app.config.security.service.CustomerUserDetailsService;
import com.caiqiu.app.config.security.util.IgnoredUrlsConfig;
import com.caiqiu.app.exception.MyException;
import com.caiqiu.app.utils.JwtUtils;
import com.caiqiu.app.utils.ResultCode;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token验证过滤器
 */
@Data
@Component
public class CheckTokenFilter extends OncePerRequestFilter {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private CustomerUserDetailsService customerUserDetailsService;
    @Resource
    private LoginFailureHandler loginFailureHandler;
    @Resource
    private RedisService redisService;

    @Resource
    private IgnoredUrlsConfig ignoredUrlsConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//
//
//        } catch (AuthenticationException e) {
//            //System.out.println(e.getMessage());
//            //loginFailureHandler.onAuthenticationFailure(request, response, e);
//        }
        this.validateToken(request);


        //登录请求不需要验证token
        doFilter(request,response,filterChain);
        //filterChain.doFilter(request,response);
    }
    /**
     * 验证token
     *
     * @param request
     */
    private void validateToken(HttpServletRequest request) throws AuthenticationException {
        //从头部获取token信息
        String token = request.getHeader("token");
        //如果请求头部没有获取到token，则从请求的参数中进行获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        //如果请求参数中也不存在token信息，则抛出异常
        if (!ObjectUtils.isEmpty(token)) {
            //如果存在token，则从token中解析出用户名
            String username = jwtUtils.getUsernameFromToken(token);
            //判断是否username为空
            if (username == null) {
                throw new CustomerAuthenticationException("token认证失败，无法访问系统资源，请先登录!");
            }
            //获取用户信息
            UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
            //创建身份验证对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //设置到Spring Security上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}