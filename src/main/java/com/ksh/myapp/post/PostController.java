package com.ksh.myapp.post;


import com.ksh.myapp.auth.Auth;
import com.ksh.myapp.auth.AuthProfile;
import com.ksh.myapp.auth.entity.LoginRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "posts")
public class PostController {
    @Autowired
    PostRepository repo;
    @Autowired
    LoginRepository logRepo;
//    @Autowired
//    PostCommentRepository commentRepo;
//    @Autowired
//    PostService service;

    @GetMapping
    public List<Post> getPostList() {

        // repository query creation을 이용한 방법
        List<Post> list = repo.findPostSortByNo();
        System.out.println("1"+list);
        return list;

    }
    @GetMapping(value = "/paging")
    public Page<Post> getPostsPaging(@RequestParam int page, @RequestParam int size) {
        System.out.println(page + "1");
        System.out.println(size + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findAll(pageRequest);
    }

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

        post.setCreatorId(authProfile.getId());
        //생성된 객체를 반환
        Post savedPost = repo.save(post);
        System.out.println(savedPost);


        //생성된 객체가 존재하면 null값이 아닐 때
        if (savedPost != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("data", savedPost);
            res.put("message", "created");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }


        return ResponseEntity.ok().build(); // 이렇게하면 그냥 생성되고 끝!
    }

    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no, @RequestAttribute AuthProfile authProfile) {
        System.out.println(no);

        Optional<Post> post = repo.findPostByNo(no);


        if (!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (post.get().getNo() != no) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyPost(@PathVariable long no, @RequestBody PostModifyRequest post, @RequestAttribute AuthProfile authProfile) {
        System.out.println(no);
        System.out.println(post);

        // 1. 키값으로 조회해옴
        Optional<Post> findedPost = repo.findById(authProfile.getId());
        // 2. 해당 레코드가 있는지 확인
        if (!findedPost.isPresent()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //수정해서 저장할 포스트
        Post toModifyPost = findedPost.get();

        if (post.getTitle() != null && !post.getTitle().isEmpty()) {
            toModifyPost.setTitle(post.getTitle());
        }
        if (post.getReview() != null && !post.getReview().isEmpty()) {
            toModifyPost.setReview(post.getReview());
        }
        //update
        repo.save(toModifyPost);

        //ok 처리
        return ResponseEntity.ok().build();
    }
//    @Auth
//    @PostMapping("/{no}/comments")
//    public ResponseEntity addComments(
//            @PathVariable long no,
//            @RequestBody PostComment postComment,
//            @RequestAttribute AuthProfile authProfile) {
//
//        Optional<Post> post = repo.findById(no);
//        if(!post.isPresent()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        // 커멘트 추가
//        postComment.setPost(post.get());
////        postComment.setOwnerId(authProfile.getId());
////        postComment.setOwnerName(authProfile.getNickname());
//
//        // 커멘트 건수 증가 및 최근 커멘트 표시
//        Post findedPost = post.get();
//        findedPost.setLatestComment(postComment.getContent());
//        findedPost.setCommentCnt(post.get().getCommentCnt() + 1);
//
//        // 트랜잭션 처리
//        service.createComment(findedPost, postComment);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }



}
