package com.ksh.myapp.auth.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
        private MultipartFile businessCard;

}
