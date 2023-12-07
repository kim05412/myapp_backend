package com.ksh.myapp.post;


import com.ksh.myapp.auth.Auth;
import com.ksh.myapp.auth.AuthProfile;
import com.ksh.myapp.auth.entity.LoginRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name="포스트 관리 및 처리 | API")
@RestController
@RequestMapping(value = "api/app/posts")
public class PostController {
    @Autowired
    PostRepository repo;
    @Autowired
    LoginRepository loginRepo;
//    @Autowired
//    PostCommentRepository commentRepo;
//    @Autowired
//    PostService service;

    @Operation(summary = "포스트 목록 조회", security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping
    public List<Post> getPostList() {

        // repository query creation을 이용한 방법
        List<Post> list = repo.findPostSortByNo();
        return list;

    }

    @Operation(summary = "포스트 작성", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost( @RequestBody Post post, @RequestAttribute AuthProfile authProfile) {

        //1. 입력값 자동 검증
        // -> 입력값 오류(빈 값)가 있으면 400 에러 띄움
        //제목과 컨텐트 내용이 비어있으면 내보내는 400번 코드
        System.out.println(authProfile);
        System.out.println("포스트 작성");

        if (post.getTitle() == null || post.getReview() == null || post.getTitle().isEmpty() || post.getReview().isEmpty()) {
//
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //3. 시간값, 게시자 이름 설정(set필드명(..))

        post.setNickname(authProfile.getNickname());
        post.setCompanyName(authProfile.getCompanyName());
        post.setCompanyAddress(authProfile.getCompanyAddress());
        post.setCreatedTime(new Date().getTime());

        //생성된 객체를 반환
        Post savedPost = repo.save(post);


        //생성된 객체가 존재하면 null값이 아닐 때
        if (savedPost != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("data", savedPost);
            res.put("message", "created");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }


        return ResponseEntity.ok().build(); // 이렇게하면 그냥 생성되고 끝!
    }
}
