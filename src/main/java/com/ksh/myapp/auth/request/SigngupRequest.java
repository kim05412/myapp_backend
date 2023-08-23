package com.ksh.myapp.auth.request;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SigngupRequest {
        private String username;
        private String password;
        private String nickname;
        private int birthyear;
        private String company;
        private String address;
        private String img;

}
