package com.ksh.myapp.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//JpaRepository의 제네릭 타입
// 엔티티의 주 키(primary key):래퍼 클래스
@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {  //PostRepository는 데이터베이스와 상호작용
    // DB에서 menuType으로 먼저 정렬-> 숫자로 정렬
    @Query(value = "SELECT * FROM post ORDER BY type ASC, no ASC", nativeQuery = true)
    List<Post> findAllPostSortByMenuTypeAndNo();

    // 매뉴타입로 데이터 검색
    @Query(value = "select * from post where type = :type", nativeQuery = true)
    Optional<Post> findAllPostByType(Integer type);

}
