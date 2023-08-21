package com.ksh.myapp.Auth.entity;

import jakarta.persistence.Column;

public class Login {
    @Column(unique = true)
    private String username;

    @Column(length = 500)
    private String secret;

    // 관계테이블에 키값만 저장
    private long profileId;

}
