package com.ksh.myapp.auth.entity;

import com.ksh.myapp.auth.Auth;
import com.ksh.myapp.auth.AuthProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class profileController {
    @Autowired
    ProfileRepository repo;

    // 클라이언트로부터 받은 닉네임 값-> 회원을 검색하고 결과를 반환
    @PutMapping(value = "/{nickname}")
    public ResponseEntity<Map<String,Object>>getProfile(@PathVariable String nickname){

        System.out.println("입력값 확인 : " + nickname);
        Optional<Profile> profile = repo.findByNickname(nickname);

        Map<String, Object> res = new HashMap<>();
        if (profile.isPresent()) {
            res.put("data", profile);
            res.put("message", "FOUND");

            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            res.put("data", null);
            res.put("message", "NOT_FOUND");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }}

    // 프로필(전체) 정보 조회
    @Auth
    @GetMapping(value="/getUserInfo")
    public ResponseEntity<AuthProfile> getAuthProfile(@RequestAttribute AuthProfile authProfile) {

        System.out.println("authProfile :"+authProfile);

        return ResponseEntity.status(HttpStatus.OK).body(authProfile);

    }

}
