package com.example.qingge_springboot.config;

import com.example.qingge_springboot.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/register","/file/**","/avatar/**","/api/goods/**","/api/icon/**","/api/category/**","/api/market/**","/api/carousel/**")
        ;
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
