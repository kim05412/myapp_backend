package com.ksh.myapp.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Signin {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    // pk란? : column of row in the table to identify
    @Column(unique = true)
    // 중복 이름 허용X
    private String username;
    @Column(length = 500)
    private String secret;
    private long profileId;
}
