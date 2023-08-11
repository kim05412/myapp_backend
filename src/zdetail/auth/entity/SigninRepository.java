package com.ksh.myapp.auth.entity;

import org.springframework.data.jpa.repository.JpaRepository;
// JpaRepository -> SigninRepository = Spring Data JPA -> 데이터베이스 작업
import java.util.Optional;


public interface SigninRepository extends JpaRepository<Signin, Long> {
    // DB 작업 가능
    // JpaRepository<entity,type of key>
    //*Entity: real data. ex) id, name, address, ... rowof table을 구성하는 속성들

    ////커스텀 메소드 : JpaRepository에서 제공하지 않는 기능을 구현하기 위한 것
    Optional<Signin> findByUsername(String username);
    // 값이 존재 O or X때 -> NullPointerException을 방지
    // username 받아서 signin의 DB조회-> signindml 속성(틀:name) 인스턴tm(real data:"john")찾음.


}
