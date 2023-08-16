package com.ksh.myapp.auth.entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository  extends JpaRepository<Login, Long> {
    // 1건이거나 없거나
    // Optional.isPresent()
    Optional<Login> findByUsername(String username);
}
