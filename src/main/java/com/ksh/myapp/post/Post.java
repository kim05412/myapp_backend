package com.ksh.myapp.post;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
public class Post {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String menu;
    @Column(nullable = false)
    private String dish;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String content;

    private String creatorName;
    private long creatorId;
    private Long createdTime;


}
