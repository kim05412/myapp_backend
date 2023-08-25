package com.ksh.myapp.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Auth {
    // 역할(일반사용자, 관리자)
    // @Auth(role="GOLD")
//    public String role();
    public boolean require() default true;
}