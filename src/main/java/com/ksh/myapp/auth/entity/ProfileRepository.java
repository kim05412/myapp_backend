package com.ksh.myapp.auth.entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //JpaRepository<Profile, Long>:도메인 클래스와 그 도메인 클래스의 기본 키 타입을 지정
public interface ProfileRepository extends JpaRepository<Profile, Long> {
        //Profile 테이블에서 Login 객체와 연결된 Profile 객체를 찾음.
        Optional<Profile> findByLogin_Id(long id);
        Optional<Profile> findByNickname(String nickname);
}
