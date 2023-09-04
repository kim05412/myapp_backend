package com.ksh.myapp.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private long year;

}

