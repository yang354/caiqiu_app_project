package com.caiqiu.app.config.swagger;

import java.util.ArrayList;
import java.util.List;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.models.auth.In;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger3的接口配置
 * 
 * @author yyyz
 */
@Profile({"dev","prod"})
@Configuration
public class SwaggerConfig
{
    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.OAS_30)
                //是否启用swagger
                .enable(true)
                .groupName("webApi")
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()

                //通过apis方法配置要扫描的controller的位置  因为配置了全局扫描指定的包了，所以可以不用在这里指定扫描
                //.apis(RequestHandlerSelectors.basePackage("com.yyyz.web"))

                //通过paths方法配置路径
                .paths(PathSelectors.any())
                //设置需要排除的路径(如果需要)
                //不显示错误的接口地址
                .paths(PathSelectors.regex("/error").negate()) //swagger3//错误路径不监控
                .build()
                /*
                设置安全模式，swagger可以设置访问token，这样方便每次访问的时候不用自己在手动在请求头加入token
                */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

//############################################固定写法########################################################
    /**
     * 安全模式，这里指定token通过token头请求头传递
     */
    private List<SecurityScheme> securitySchemes()
    {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey("token", "token", In.HEADER.toValue()));
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts()
    {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                        .build());
        return securityContexts;
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("token", authorizationScopes));
        return securityReferences;
    }
//####################################################################################################
    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo()
    {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                .title("彩球小程序API文档")
                .description("本文档描述了彩球小程序接口定义")
                .version("1.0")
                .contact(new Contact("java", "http://yyyz.com", "yyyz@qq.com"))
                .build();
    }
}
