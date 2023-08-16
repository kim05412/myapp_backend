package com.ksh.myapp.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String CompanyName;
    private String CompanyAdress;
    private String name;
    private int Birthyear;

    @Column(nullable = false)
    private String nickname;
    private String email;


    @OneToOne
    private Signin signin;
    @OneToOne
    private Login login;
}
