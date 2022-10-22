package com.example.qingge_springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.qingge_springboot.mapper")
@SpringBootApplication
public class QinggeSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(QinggeSpringbootApplication.class, args);
    }

}
