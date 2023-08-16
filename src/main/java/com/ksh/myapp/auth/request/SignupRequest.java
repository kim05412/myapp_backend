package com.ksh.myapp.auth.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private String nickname;
    private String email;
}
