package com.ksh.myapp.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 회원가입 후에-> 입력된 profile 정보 중 인증에 사용되는 프로필 정보만
public class AuthProfile {
    private long id;         // 프로필 id
    private String nickname; // 프로필 별칭
    private String username; // 로그인 사용자이름
}
