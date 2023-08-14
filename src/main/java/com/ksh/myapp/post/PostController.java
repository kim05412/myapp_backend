package com.ksh.myapp.post;

import com.ksh.myapp.auth.Auth;
import com.ksh.myapp.auth.AuthProfile;
import com.ksh.myapp.auth.entity.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public class PostController {
    @Autowired
    PostRepository repo;
    @Autowired
    LoginRepository logRepo;

//    @Autowired
//    PostService service;
    @Autowired
    PostRepositorySupport repoSupport;

    // posts 경로에 대한 모든 게시글 조회
    @GetMapping
    public List<Post> getPostList() {
        //Query 사용
        List<Post> list = repo.findPostSortByNo();
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


    @GetMapping(value = "/paging/search")
    public Page<Post> getPostsPagingSearch
    (@RequestParam int page, @RequestParam int size, @RequestParam String query) {
        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return repoSupport.searchPaging(query, pageRequest);
    }

    @Auth
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post, @RequestAttribute AuthProfile authProfile) {

        System.out.println(authProfile);

        if (post.getTitle() == null || post.getContent() == null || post.getTitle().isEmpty() || post.getContent().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }



        post.setCreatorName(authProfile.getNickname());
        post.setCreatedTime(new Date().getTime());

        post.setCreatorId(authProfile.getId());

        Post savedPost = repo.save(post);


        if (savedPost != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("data", savedPost);
            res.put("message", "created");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }


        return ResponseEntity.ok().build();
    }

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
        if (post.getContent() != null && !post.getContent().isEmpty()) {
            toModifyPost.setContent(post.getContent());
        }
        //update
        repo.save(toModifyPost);

        //ok 처리
        return ResponseEntity.ok().build();
    }

    public ResponseEntity addComments(
            @PathVariable long no,
            @RequestBody PostComment postComment,
            @RequestAttribute AuthProfile authProfile) {

        Optional<Post> post = repo.findById(no);
        if(!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        postComment.setPost(post.get());
        postComment.setOwnerId(authProfile.getId());
        postComment.setOwnerName(authProfile.getNickname());

        Post findedPost = post.get();
        findedPost.setLatestComment(postComment.getContent());
        findedPost.setCommentCnt(post.get().getCommentCnt() + 1);

        service.createComment(findedPost, postComment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
