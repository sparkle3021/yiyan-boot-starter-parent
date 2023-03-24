package com.oho.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MyBatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBatisApplication.class, args);
    }

}
