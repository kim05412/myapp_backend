package com.ksh.myapp.post;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Data
@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostRepository repo;

    @Autowired // 자동 초기화
    private PostRepository postRepository; // 멤버 변수

    // 메뉴타입으로 조회
    @GetMapping
    public List<Post> getPostList(@RequestParam Integer type) {
        List<Post> list = repo.findAllPostByType(type);
        return list;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post) {
        if (post.getTitle() == null || post.getName() == null || post.getReviewContent() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

//        post.setCreatorName(authProfile.getNickname());
        post.setCreatedTime(new Date().getTime());

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


//postRepository->엔티티를 데이터베이스에서 조회=>반환.
@GetMapping
public List<Post> findAll() {
    return postRepository.findAll();
}


    // 포스트 DB에 추가
//    @Auth
//    @PostMapping("/{no}")
}
