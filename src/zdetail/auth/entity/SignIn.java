package com.ksh.myapp.auth;
// jpa API
import jakarta.persistence.*;
//- 데이터베이스와 상호작용하기 위한 클래스들
// : 영속성 관련 라이브러리를 가져옴

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class SignIn {
    @Id
    //기본키 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본 키 값이 자동으로 생성되며, 데이터베이스가 알아서 값을 할당
    private long id;
    //id = pk = 해당 테이블의 기본키
    // pk란? : column of row in the table to identify
    @Column(unique = true)
    // 중복 이름 허용X
    private String username;
    @Column(length = 500)
    private String secret;
    private long profileId;
}
