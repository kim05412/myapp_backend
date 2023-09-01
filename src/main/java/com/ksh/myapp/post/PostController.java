package com.ksh.myapp.post;

import com.ksh.myapp.auth.Auth;
import com.ksh.myapp.auth.AuthProfile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


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
        return repo.findBySelectedMenuTypesContains(query, pageRequest);
    }
//추가
    @Auth
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post, @RequestAttribute AuthProfile authProfile) {
        String[] selectedMenuTypes = post.getSelectedMenuTypes();
        String[] loadedFiles = post.getLoadedFiles();
        String title = post.getTitle();
        String menu = post.getMenu();
        String address = post.getAddress();
        String review = post.getReview();


        // 첫 번째 파일 업로드 여부 확인
        if (loadedFiles == null || loadedFiles.length < 1 || loadedFiles[0].isEmpty()) {
            Map<String, Object> res = new HashMap<>();
            res.put("message", "첫 번째 이미지 파일은 필수로 업로드해야 합니다.");
            System.out.println("첫 번째 이미지 파일은 필수로 업로드");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        // 필수 정보 입력 검증
        if (selectedMenuTypes == null || selectedMenuTypes.length == 0 ||
                title == null || title.isEmpty() ||
                menu == null || menu.isEmpty() ||
                address == null || address.isEmpty() ||
                review == null || review.isEmpty()) {
            Map<String, Object> res = new HashMap<>();
            res.put("message", "잘못된 정보 입력됨");
            System.out.println("잘못된 정보 입력됨");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        Post savedPost = new Post(); // 여기서는 가정한 클래스명
        savedPost.setSelectedMenuTypes(selectedMenuTypes);
        savedPost.setLoadedFiles(loadedFiles);
        savedPost.setTitle(title);
        savedPost.setMenu(menu);
        savedPost.setAddress(address);
        savedPost.setReview(review);
        savedPost.setCreatedTime(new Date().getTime());

        Map<String, Object> res = new HashMap<>();
        res.put("data", savedPost);
        res.put("message", "created");
//        return ResponseEntity.status(HttpStatus.CREATED).body(res);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(ServletUriComponentsBuilder
                        .fromHttpUrl("http://localhost:5502")
                        .build().toUri())
                .build();
        // 좋아요 기능 추가

// formData 형식
//    public ResponseEntity<Map<String, Object>> addPost(
//            @RequestParam(value = "selectedMenuTypes[]") String[] selectedMenuTypes,
//            @RequestParam(value = "loadedFiles[]") String[] loadedFiles,
//            @RequestParam("title") String title,
//            @RequestParam("menu") String menu,
//            @RequestParam("address") String address,
//            @RequestParam("review") String review
//    )  {
//        System.out.println(selectedMenuTypes);
//        System.out.println(loadedFiles);
//
//        // 첫 번째 파일 업로드 여부 확인
//        if (loadedFiles.length < 1 || loadedFiles[0].isEmpty()) {
//            Map<String, Object> res = new HashMap<>();
//            res.put("message", "첫 번째 이미지 파일은 필수로 업로드해야 합니다.");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//        }
//        // 필수 정보 입력 검증
//        if (selectedMenuTypes == null || selectedMenuTypes.length == 0 ||
//                title == null || title.isEmpty() ||
//                menu == null || menu.isEmpty() ||
//                address == null || address.isEmpty() ||
//                review == null || review.isEmpty()) {
//            Map<String, Object> res = new HashMap<>();
//            res.put("message", "잘못된 정보 입력됨");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//        }
//        // 선택한 메뉴 타입 리스트로 변환
//        List<String> selectedMenuTypeList = Arrays.asList(selectedMenuTypes);
//        List<String> loadedFiles = Arrays.asList(selectedMenuTypes);
//
//        Post savedPost = new Post(); // 여기서는 가정한 클래스명
//        savedPost.setTitle(title);
//        savedPost.setMenu(menu);
//        savedPost.setAddress(address);
//        savedPost.setReview(review);
//        savedPost.setCreatedTime(new Date().getTime());
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", savedPost);
//        res.put("message", "created");
//        return ResponseEntity.status(HttpStatus.CREATED).body(res);
//        //좋아요 기능 추가
        }
    }


//postRepository->엔티티를 데이터베이스에서 조회=>반환.
//@GetMapping
//public List<Post> findAll() {
//    return postRepository.findAll();
//}


    // 포스트 DB에 추가
//    @Auth
//    @PostMapping("/{no}")

