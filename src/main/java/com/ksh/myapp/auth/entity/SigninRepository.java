package com.ksh.myapp.auth.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SigninRepository extends JpaRepository<Signin, Long> {
    Optional<Signin> findByUsername(String username);
}
