package com.ksh.myapp.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

//JpaRepository의 제네릭 타입
// 엔티티의 주 키(primary key):래퍼 클래스
@Repository //DB access
public interface PostRepository extends JpaRepository<Post,Long> {  //PostRepository는 데이터베이스와 상호작용
    Page<Post> findBySelectedOptionsContains(List<String> selectedOptions, Pageable pageable);

    // 기본 포스팅 정렬
    @Query(value = "select * from post order by no",nativeQuery = true)
    List<Post> findPostsSortByNo();

    //post 테이블에서 사용자가 선택한 타입만 검색-> 열을 오름차순으로 정렬
    @Query(value = "SELECT * FROM post WHERE selectedOptions = :selectedOptions ORDER BY no ASC", nativeQuery = true)
    List<Post> findPostsBySelectedOptions(@Param("selectedOptions") Integer selectedOptions);

    // 매뉴타입로 데이터 검색->최신순 1개만 가져옴
    @Query(value = "SELECT * FROM post WHERE selectedOptions = :selectedOptions ORDER BY no ASC LIMIT 1", nativeQuery = true)
    List<Post> findPostBySelectedOptions(@Param("selectedOptions") Integer selectedOptions);

    Optional<Post> findPostByNo(Long no);
    Page<Post> findByMenuContains(String menu, Pageable pageable);

    Page<Post> findByNo(long no, Pageable pageable);
//    Page<Post> findById(long id, Pageable pageable);
    Page<Post> findByMenuContainsOrSelectedOptionsContains(String menu, List<String>selectedOptions, Pageable pageable);}
