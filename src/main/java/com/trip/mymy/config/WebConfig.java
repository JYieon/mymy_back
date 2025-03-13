package com.trip.mymy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring의 설정 클래스임을 명시
public class WebConfig implements WebMvcConfigurer {

    // 정적 리소스 핸들러 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Favicon 요청을 무시 (없어도 404 에러 발생 안 하게)
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
    }

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true); // 인증 정보 포함 가능
    }
}
