package com.ksh.myapp.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor //빈 생성자
@AllArgsConstructor //초기화
@Builder  // 타입에 맞춰 자동 할당
@Data
@Entity  //JPA->DB 테이블 연결
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;
    @Column(nullable = false)
//@NotBlank(message = "제목을 입력해주세요.")
    private String title;
    //컬럼크기 1024byte * 1024 = 1mb * 20 = 20mb
    @Column(length = 1024 * 1024 * 20) // MySQL에서는 longtext로 바뀜
//    바뀜
    // 파일을 base64 data-url 문자열로 저장
//    @NotBlank(message = "이미지을 입력해주세요.")
    private String image;

    @Column(nullable = false)
//    @NotBlank(message = "타입을 입력해주세요.")
    private String selected;
    @Column(nullable = false)
//    @NotBlank(message = "메뉴를 입력해주세요.")
    private String menu;
    @Column(nullable = false)
//    @NotBlank(message = "리뷰를 입력해주세요.")
    private String review;
    @Column(nullable = false)
//    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    private Long year;
    private String companyName;
    private String companyAddress;
    private String nickname;
    private Long createdTime; // null값 가능

//    // 댓글 수
//    private long commentCnt;
//    // 최근 댓글 내용
//    private String latestComment;

//    private String file;
//    @ElementCollection
//    private List<String> selectedOptions; // 메뉴 옵션 선택
//    @ElementCollection
//    private List<String> loadedFiles; // 사진


//    @Column(length = 1024 * 1024 * 20) // MySQL에서는 longtext로 바뀜
//    // 파일을 base64 data-url 문자열로 저장
//    private String loadedFiles;






}
