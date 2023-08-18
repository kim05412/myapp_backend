package com.ksh.myapp.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//JpaRepository의 제네릭 타입
// 엔티티의 주 키(primary key):래퍼 클래스
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {  //PostRepository는 데이터베이스와 상호작용
}
