package com.innovation.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000")
        .allowedOrigins("https://earthus.vercel.app")
        .allowedOrigins("https://main.d1bjeqt1vblv2f.amplifyapp.com")
        .allowedOrigins("https://accounts.google.com/o/oauth2/v2/**")
        .allowedOrigins("http://earth-us.s3-website.ap-northeast-2.amazonaws.com")
        .allowedOrigins("ws://localhost:8080/ws")
        .allowedOrigins("http://localhost:8080/ws")
        .allowedOrigins("https://earthus.vercel.app")
        .allowedMethods("*") // 기타 설정
        .allowedHeaders("*")
        .allowCredentials(true)
        .exposedHeaders("Authorization")
        .exposedHeaders("Refresh-Token")
        .maxAge(3600);
  }
}
