package com.ksh.myapp.auth.entity;

import jakarta.persistence.*;

public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String CompanyName;
    private String CompantAdress;
    private String name;
    private int Birthyear;

    @Column(nullable = false)
    private String nickname;
    private String email;


    @OneToOne
    private Signin signin;
}
