package com.homeworkSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@EnableAsync  //开启异步注解功能
@EnableScheduling //开启基于注解的定时任务
public class HomeworkSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(HomeworkSystemApplication.class, args);
    }
}
