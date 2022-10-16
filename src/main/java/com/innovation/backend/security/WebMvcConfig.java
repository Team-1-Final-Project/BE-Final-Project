package com.innovation.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("https://earthus.vercel.app")
            .allowedOrigins(" https://main.d1bjeqt1vblv2f.amplifyapp.com/")
            .allowedMethods("*") // 기타 설정
            .allowedHeaders("*");
    }
}
