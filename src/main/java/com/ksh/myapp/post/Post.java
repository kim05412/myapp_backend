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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    DB의 자동 증가(auto-increment)-> 기본 키(primary key) 값을 생성
    private long id; //null(x)
    private int menuType;
    private String title;
    private String menuName;
    private String address;
    private String reviewContent;


}
