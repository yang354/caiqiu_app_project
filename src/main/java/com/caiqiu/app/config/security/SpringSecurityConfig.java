package com.caiqiu.app.config.security;




import com.caiqiu.app.config.security.filter.CheckTokenFilter;
import com.caiqiu.app.config.security.handler.AnonymousAuthenticationHandler;
import com.caiqiu.app.config.security.handler.LoginFailureHandler;
import com.caiqiu.app.config.security.handler.LoginSuccessHandler;
import com.caiqiu.app.config.security.service.CustomerUserDetailsService;
import com.caiqiu.app.config.security.util.IgnoredUrlsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CustomerUserDetailsService customerUserDetailsService;

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Resource
    private AnonymousAuthenticationHandler anonymousAuthenticationHandler;

    @Resource
    private CheckTokenFilter checkTokenFilter;

    @Resource
    private IgnoredUrlsConfig ignoredUrlsConfig;

    /**
    * 注入加密处理类
    * @return
    */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    //注入白名单对象
//    @Bean
//    public IgnoredUrlsConfig ignoredUrlsConfig(){
//        return new IgnoredUrlsConfig();
//    }


    /**
     * 处理登录认证
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        // 循环白名单进行放行
        for (String url : ignoredUrlsConfig.getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        //登录前进行过滤
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //登录前进行过滤
        http.formLogin()  //表单登录
                //注意指定了登录url，使用@RepeatSubmit防重复提交注解则不会生效，只有当不指定时为普通的url就可以生效
                //.loginProcessingUrl("/api/user/login")  //登录请求url地址，自定义即可
                // 设置登录验证成功或失败后的的跳转地址
                .successHandler(loginSuccessHandler)  //认证成功处理器
                .failureHandler(loginFailureHandler) //认证失败处理器
                // 禁用csrf防御机制
                .and().csrf().disable()
                //不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //设置需要拦截的请求
                .authorizeRequests()
                //其他一律请求都需要进行身份认证
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                //匿名无权限访问   就是表示你没有登录访问资源会触发
                .authenticationEntryPoint(anonymousAuthenticationHandler)

                .and().cors();//开启跨域配置

    }




    /**
     * 配置认证处理器
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
