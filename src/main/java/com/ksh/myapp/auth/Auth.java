package com.ksh.myapp.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)  //런타임시에도 어노테이션 정보 유지되어야한다.
@Target({ElementType.METHOD})  // 어노테이션 적용대상 지정 :method
public @interface Auth {      //어노테이션 정의
    public boolean require() default true;
    // 해당 메서드에 접근 하려면 인증이 필요하다.
}
