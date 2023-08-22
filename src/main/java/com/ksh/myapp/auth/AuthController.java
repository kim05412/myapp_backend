package com.ksh.myapp.auth;

import com.ksh.myapp.auth.entity.LoginRepository;
import com.ksh.myapp.auth.entity.ProfileRepository;
import com.ksh.myapp.auth.request.SigngupRequest;
import com.ksh.myapp.auth.util.HashUtil;
import com.ksh.myapp.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class AuthController {
    @Autowired
    private LoginRepository repo;
    @Autowired
    private ProfileRepository profileRepo;
    @Autowired
    private AuthService service;
    @Autowired
    private HashUtil hash;

    @Autowired
    private JwtUtil jwt;

    @PostMapping(value = "/signup")
    public ResponseEntity signUp(@RequestBody SigngupRequest.SignupRequest req) {
        System.out.println(req);

        // 1. Validation
        // 입력값 검증
        // 사용자이름없거나/중복이거나, 패스워드없거나, 닉네임, 이메일 없음...

        // 2. Buisness Logic(데이터 처리)
        // profile, login 생성 트랜잭션 처리
        long profileId = service.createIdentity(req);

        // 3. Response
        // 201: created
        return ResponseEntity.status(HttpStatus.CREATED).body(profileId);
    }


}
