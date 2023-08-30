package com.ksh.myapp.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor //빈 생성자
@AllArgsConstructor //초기화
@Builder  // 타입에 맞춰 자동 할당
@Data
@Entity  //JPA->DB 테이블 연결
public class Post {
    @Id // DB 테이블의 PK로 설정
//    private String nickname; // 계정 Id, 인터넷세계의 집주소(불변)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    DB의 자동 증가(auto-increment)-> 기본 키(primary key) 값을 생성
    private Long no; //null(x)
    @Column(nullable = false)
    private String[] selectedMenuTypes;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String menu;
    @Column(length = 1024 * 1024 * 20) // MySQL에서는 longtext로 바뀜
    // 파일을 base64 data-url 문자열로 저장
    private String[] loadedFiles;

//    private String companyname;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String review;

    private Long createdTime; // null값 가능



}
