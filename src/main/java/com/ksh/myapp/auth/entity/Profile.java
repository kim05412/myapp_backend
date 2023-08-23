package com.ksh.myapp.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //-> service에서 저장된 정보 불러오기
@NoArgsConstructor
@AllArgsConstructor
@Builder // -> service에서 build 패턴 가능
@Entity // db 프로핑 정보 데이터 저장-> 인증 기능 가능하게
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nickname;
    private int birthyear;
    private String company;
    private String address;

    @OneToOne(fetch = FetchType.LAZY) // 포스트 조회와 동시에 모든 포스트의 작성자정보 다 불러옴-> 비효율적
    //  profile_id 컬럼이 FK로 생성됨 :signup->login
    private Login login;
}
