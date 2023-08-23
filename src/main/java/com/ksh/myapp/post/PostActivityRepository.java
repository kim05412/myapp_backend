package com.ksh.myapp.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostActivityRepository extends JpaRepository<PostActivity, Long> {
}
