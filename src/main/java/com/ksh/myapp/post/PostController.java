package com.ksh.myapp.post;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Data
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    @Autowired
    PostRepository repo;

    @Autowired // 자동 초기화
    private PostRepository postRepository; // 멤버 변수

    // 기본: 최신순 조회
    @GetMapping(value = "/paging")
    public Page<Post> getPostsPaging(@RequestParam int page, @RequestParam int size){
        System.out.println(page);
        System.out.println(size);

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findAll(pageRequest);
    }

    @GetMapping(value = "/paging/search")
    public Page<Post> getPostsPagingSearch(@RequestParam int page, @RequestParam int size, @RequestParam String query){
        System.out.println(query + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findByTypeContains(query, pageRequest);
    }
//추가
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post) {
        System.out.println(post);
        // 필수 정보 입력 검증
        if (post.getType() == null || post.getTitle().isEmpty() ||post.getTitle() == null || post.getAddress().isEmpty() || post.getAddress() == null || post.getAddress().isEmpty()||post.getReview() == null) {
            Map<String,Object> res = new HashMap<>();
            res.put("message", "잘못된 정보 입력됨");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

//      post.setAddress(authProfile.getAddress()); 회사명 또는 회사 주소 자동 표시
//      post.setYear(authProfile.getYear()); -> 사용자들에게는 안보이고 연령대 통계에서만 사용
//        post.set(authProfile.getNickname());
        post.setCreatedTime(new Date().getTime());
        // 좋아요 수 표시
        //좋아요 기능 추가

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
//@GetMapping
//public List<Post> findAll() {
//    return postRepository.findAll();
//}


    // 포스트 DB에 추가
//    @Auth
//    @PostMapping("/{no}")
}
