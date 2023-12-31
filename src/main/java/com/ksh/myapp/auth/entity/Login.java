package com.ksh.myapp.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String userId;

//로그인시 입력 안할거면 제거
    @Column(unique = true)
    private String nickname;


    @Column(length = 500)
    private String secret;

    // 로그인시 회원 가입시에 만들어진 id ->관계테이블에 키값만 저장
    private long profileId;


}
