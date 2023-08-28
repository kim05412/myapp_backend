package com.ksh.myapp.auth.configuration;

import com.ksh.myapp.auth.util.HashUtil;
import com.ksh.myapp.auth.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    @Bean
    public HashUtil hashUtil() {
        return new HashUtil();
    }

    @Bean
    public JwtUtil jwtUtil() { return new JwtUtil(); }

}

