package com.ksh.myapp.Auth;


public @interface Auth {
    public boolean require() default true;
}
