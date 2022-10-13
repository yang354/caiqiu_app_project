package com.caiqiu.app.config.mybatisPlus;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.caiqiu.app.model.dao") //加载Mapper接口
@Configuration
public class MyBatisPlusConfig {
    // 最新版
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor =
                new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);//溢出后从第1页开始
        // 指定数据库类型
        interceptor.addInnerInterceptor(paginationInnerInterceptor); return interceptor;
    }



}