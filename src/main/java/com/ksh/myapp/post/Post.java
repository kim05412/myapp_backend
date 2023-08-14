package com.ksh.myapp.post;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class Post {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private String typeOfRestaurant;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private String creatorName;
    private long creatorId;

    private Long createdTime;

}
