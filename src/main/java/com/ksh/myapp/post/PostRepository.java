package com.ksh.myapp.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    //  상속받음으로써, 데이터베이스에서의 데이터 처리를 담당할 핵심 CRUD 기능 사용가능
    List<Post> findPostSortByNo();
}
