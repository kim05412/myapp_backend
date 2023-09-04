//package com.ksh.myapp.post;
//
//import com.ksh.myapp.auth.Auth;
//import com.ksh.myapp.auth.AuthProfile;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping(value = "/posts")
//public class PostController2 {
//
//    @Autowired
//   PostRepository repo;
//
//    @GetMapping
//    public List<Post> getpostList() {
//        List<Post> list = repo.findAllByOrderByNickname();
//
//        return list;
//    }
//
//    @Auth
//    @GetMapping(value = "/paging")
//    public Page<Post> getpostsPaging
//            (@RequestParam int page, @RequestParam int size,
//             @RequestAttribute AuthProfile authProfile) {
//        System.out.println(page);
//        System.out.println(size);
//        System.out.println(authProfile);
//
//        Sort sort = Sort.by("email").descending();
//
//        PageRequest pageRequest = PageRequest.of(page, size, sort);
//
//        // 해당 사용자가 소유자인 연락처 목록만 조회
//        return repo.findByOwnerId(authProfile.getId(), pageRequest);
//    }
//
//    @GetMapping(value = "/paging/search")
//    public Page<Post> getpostsPagingSearch
//            (@RequestParam int page,
//             @RequestParam int size,
//             @RequestParam String query) {
//        System.out.println(page);
//        System.out.println(size);
//        System.out.println(query);
//
//        // 기본적으로 key 정렬(default)
//        Sort sort = Sort.by("nickname").descending();
//
//        // 페이지 매개변수 객체
//        PageRequest pageRequest = PageRequest.of(page, size, sort);
//
//        return repo.findByMenuContainsOrAddressContains
//                (query, query, pageRequest);
//    }
//
//    @GetMapping(value = "/paging/searchBySelect")
//    public Page<Post> getpostsPagingSearchSelct
//            (@RequestParam int page,
//             @RequestParam int size,
//             @RequestParam long select ) {
//        System.out.println(page);
//        System.out.println(size);
//        System.out.println(select);
//
//        // 기본적으로 key 정렬(default)
//        Sort sort = Sort.by("select").descending();
//
//        // 페이지 매개변수 객체
//        PageRequest pageRequest = PageRequest.of(page, size, sort);
//
//        return repo.findBySelectContaining(select, pageRequest);
//    }
//
//    @Auth
//    @PostMapping
//    public ResponseEntity<Map<String, Object>> addPost
//            (@RequestBody Post post,
//             @RequestAttribute AuthProfile authProfile) {
//        // 클라이언트에서 넘어온 JSON이 객체로 잘 변환됐는지 확인
//        System.out.println(authProfile);
//
//        // 1. ------------ 데이터 검증 단계
//        // 이메일 필수값 검증
//        // 400: bad request
//        if(post.getNickname() == null || post.getNickname().isEmpty()) {
//            // 응답 객체 생성
//            Map<String, Object> res = new HashMap<>();
//            res.put("data", null);
//            res.put("message", "[nickname] field is required");
//
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(res);
//        }
////        post.setCreatorName(authProfile.getNickname());
//        post.setCreatedTime(new Date().getTime());
//
//        // 테이블에 저장하고 생성된 객체를 반환
//        Post savedPost = repo.save(post);
//
//        // 3. --------- 응답 처리
//
//        if(savedPost != null) {
//            Map<String, Object> res = new HashMap<>();
//            res.put("data", savedPost);
//            res.put("message", "created");
//
//            // HTTP Status Code: 201 Created
//            // 리소스가 정상적으로 생성되었음.
//            return ResponseEntity.status(HttpStatus.CREATED).body(res);
//        }
//
//        return ResponseEntity.ok().build();
//    }
//
//}
