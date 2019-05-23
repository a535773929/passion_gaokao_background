package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有请求都允许跨域
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080","http://192.168.1.161:8080")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("AUTHORIZATION")
//                .exposedHeaders()
                .maxAge(360)
                .allowCredentials(true);
    }

}
