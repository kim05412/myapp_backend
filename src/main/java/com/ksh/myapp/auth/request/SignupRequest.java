package com.ksh.myapp.auth.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
        private String userId;
        private String password;
        private String nickname;
        private Long year;
        private String companyName;
        private String companyAddress;
        private String file;

}
