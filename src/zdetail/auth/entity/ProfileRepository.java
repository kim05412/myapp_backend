package com.ksh.myapp.auth.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//DB와 상호작용을 담당하는 Repository로 사용될 것임을 스프링에게 알림
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
        Optional<Profile> findBySignin_Id(long id);
        // signin엔티티(속성)의 pk(long타입인 매개변수 id)를 넣어 DB에서 Profile테이블에서 pk의 row를 조회하고
        // 일치하는 profile 반환

        }