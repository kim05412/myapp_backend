package com.ksh.myapp.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
//import lombok.Data;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // -> service에서  db에서 해당 데이터 가져오기
@NoArgsConstructor
@AllArgsConstructor
@Builder // -> service에서 build패턴 사용 가능
@Entity  // db테이블 연동-> 인증 검증 가능
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 로그인 가능: ->service에서 id로 로그인시 프로필과 id로 연결->

    @Column(unique = true)
    private String username;

    @Column(length = 500)
    private String secret;

    // 관계테이블에 키값만 저장
    private long profileId;

}
