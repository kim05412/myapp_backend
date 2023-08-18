package com.ksh.myapp.post;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired // 자동 초기화
    private PostRepository postRepository; // 멤버 변수

//postRepository->엔티티를 데이터베이스에서 조회=>반환.
@GetMapping
public List<Post> findAll() {
    return postRepository.findAll();
}


    // 포스트 DB에 추가
//    @Auth
//    @PostMapping("/{no}")
}
