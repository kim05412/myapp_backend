package com.ksh.myapp.auth;

import com.ksh.myapp.post.Post;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthProfile {
    private long id;         // 프로필 id
    private String nickname; // 프로필 별칭
    private String userId;
    private String companyName;
    private String companyAddress;
    private Long year;
    @Column(length = 1024 * 1024 * 20)
    private String image;


}

