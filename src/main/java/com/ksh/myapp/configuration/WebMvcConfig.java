package com.ksh.myapp.configuration;

import com.ksh.myapp.auth.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://127.0.0.1:5500") // 로컬 호스트 origin 허용
                .allowedMethods("*"); // 모든 메서드 허용(GET, POST.....)
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
}
