package com.ksh.myapp.Auth.entity;

import jakarta.persistence.*;

public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nickname;
    private int birthyear;
    private String company;
    private String address;

    @OneToOne
    //  profile_id 컬럼이 FK로 생성됨 :signup->login
    private Login login;
}
