package com.homeworkSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration//配置类
@EnableSwagger2 //swagger注解
public class SwaggerConfig {

    //配置docket以配置Swagger具体参数
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(webApiInfo())
                .enable(true) //配置是否启用Swagger，如果是false，在浏览器将无法访问
                .groupName("webApi")
                .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                .apis(RequestHandlerSelectors.basePackage("com.homeworkSystem"))
                // 配置如何通过path过滤,即这里只扫描请求以/kuang开头的接口
                .paths(PathSelectors.ant("/hs/**"))
                .build();

    }
    //配置文档信息
    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("API文档")
                .description("本文档描述了后端接口定义")
                .version("v1.0")
                .contact(new Contact("liuzirui", "http://lzr.com", "294193284@qq.com"))
                .license("许可")
                .licenseUrl("xyz.com")
                .build();
    }
}
