package com.ksh.myapp.Auth.request;

import lombok.Data;
import lombok.NoArgsConstructor;

public class SigngupRequest {
    @Data
    @NoArgsConstructor
    public class SignupRequest {
        private String username;
        private String password;
        private String nickname;
        private int birthyear;
        private String company;
        private String address;
        private String img;
    }
}
