package com.ksh.myapp.auth.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    //해당 프로필 있으면 profile 반환
    Optional<Profile> findByLogin_Id(long id);
}
