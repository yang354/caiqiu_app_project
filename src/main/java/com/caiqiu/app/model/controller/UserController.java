package com.caiqiu.app.model.controller;



import com.caiqiu.app.config.redis.RedisService;
import com.caiqiu.app.config.security.service.CustomerUserDetailsService;
import com.caiqiu.app.exception.MyException;
import com.caiqiu.app.model.entity.User;
import com.caiqiu.app.model.service.UserService;
import com.caiqiu.app.utils.JwtUtils;
import com.caiqiu.app.utils.Result;
import com.caiqiu.app.utils.ResultCode;
import com.caiqiu.app.utils.StringUtils;
import com.caiqiu.app.vo.LoginVO;
import com.caiqiu.app.vo.TokenVO;
import com.caiqiu.app.vo.UserInfoVO;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
@Api(value = "用户功能",tags = "用户功能",description = "用户功能")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RedisService redisService;
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private CustomerUserDetailsService customerUserDetailsService;


    /**
     * 获取用户中心
     *
     * @return
     */
    @Operation(summary = "获取用户中心")
    @GetMapping("/userCenter")
    public Result getUserCenter() {
        // TODO: 2022/10/11
        return null;
    }


    /**
     * 登录验证
     *
     * @param loginVO
     * @return
     */
    @ApiOperation("登录验证")
    @ApiImplicitParam(name = "loginVO")
    @PostMapping("/login")
    public UserDetails login(LoginVO loginVO) {

//        //判断验证码是否正确
//        String verifyKey = "captcha_codes:" + StringUtils.nvl(loginVO.getUuid(), "");
//        System.out.println(verifyKey);
//        String captcha = redisService.getCacheObject(verifyKey);
//        redisService.deleteObject(verifyKey);
//        if (captcha == null){
//            throw new MyException(ResultCode.ERROR,"验证码已失效");  //提示验证码已失效
//        }
//        if (!loginVO.getCode().equalsIgnoreCase(captcha)){  //比较是否相等，但忽略大小写
//            throw new MyException(ResultCode.ERROR,"验证码错误");   //提示验证码错误
//        }
        //判断用户名密码是否正确
        UserDetails user = customerUserDetailsService.loadUserByUsername(loginVO.getUsername());
        return user;
    }





    /**
     * 获取用户信息
     *
     * @return
     */

    @Operation(summary = "获取用户信息")
    @GetMapping("/getInfo")
    public Result getInfo() {
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //判断authentication对象是否为空
        if (authentication == null) {
            return Result.error().msg("用户信息查询失败");
        }
        //获取用户信息
        User user = (User) authentication.getPrincipal();

        //创建用户信息对象
        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user,userInfo);

        //返回数据
        return Result.ok(userInfo);
    }


    /**
     * 刷新token
     *
     * @param request
     * @return
     */
    @Operation(summary = "刷新token")
    @Parameter(description = "HttpServletRequest",name = "request")
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        //从header中获取前端提交的token
        String token = request.getHeader("token");
        //如果header中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取身份信息
        UserDetails details = (UserDetails) authentication.getPrincipal();
        //重新生成token
        String reToken = "";
        //验证原来的token是否合法
        if (jwtUtils.validateToken(token, details)) {
            //生成新的token
            reToken = jwtUtils.refreshToken(token);
        }
        //获取本次token的到期时间，交给前端做判断
        long expireTime = Jwts.parser().setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(reToken.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
        //清除原来的token信息
        String oldTokenKey = "token_" + token;
        redisService.del(oldTokenKey);
        //存储新的token
        String newTokenKey = "token_" + reToken;
        redisService.set(newTokenKey, reToken, jwtUtils.getExpiration() / 1000);
        //创建TokenVo对象
        TokenVO tokenVo = new TokenVO(expireTime, reToken);
        //返回数据
        return Result.ok(tokenVo).msg("token生成成功");
    }


    /**
     * 用户退出
     *
     * @param request
     * @param response
     * @return
     */
    @Operation(summary = "用户退出")
    @Parameters({
            @Parameter(description = "HttpServletRequest",name = "request"),
            @Parameter(description = "HttpServletResponse",name = "response")
    })
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        //获取token
        String token = request.getParameter("token");
        //如果没有从头部获取token，那么从参数里面获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getHeader("token");
        }
        //获取用户相关信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //清空用户信息
            new SecurityContextLogoutHandler().logout(request, response,authentication);
            //清空redis里面的token
            String key = "token_" + token;
            redisService.del(key);
        }
        return Result.ok().msg("用户退出成功");
    }

}

