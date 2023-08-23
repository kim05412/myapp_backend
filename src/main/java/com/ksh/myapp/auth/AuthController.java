package com.ksh.myapp.auth;

import com.ksh.myapp.auth.entity.LoginRepository;
import com.ksh.myapp.auth.entity.ProfileRepository;
import com.ksh.myapp.auth.request.SigngupRequest;
import com.ksh.myapp.auth.util.HashUtil;
import com.ksh.myapp.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //HTTP메서드(GET,POST,PUT,DELETE) -> 메소드의 반환값은 HTTP 응답 본문(Body)에 직접 작성:jason형식
@RequestMapping("/auth") //내부의 모든 요청 핸들링 메서들의 URL 접두사
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

    //회원가입 요청 처리
    @PostMapping(value = "/signup") //jason->java 형태
    public ResponseEntity signUp(@RequestBody SigngupRequest req) {
        // 입력 받은 회원가입 정보
        System.out.println(req);

        // 1.Validation(입력값 검증): interceptor -> 인증 필요한 요청 중 검증 완료 후-> controller로 전달

        // 2. Business Logic(DB데이터 처리):service->회원가입시, 로그인-프로필 객체 일관성(트랜잭션 처리)
        long profileId = service.createIdentity(req);

        // 3. Response
        // 201: created
        return ResponseEntity.status(HttpStatus.CREATED).body(profileId);
    }


}
