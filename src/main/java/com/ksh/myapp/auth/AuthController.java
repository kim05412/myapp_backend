package com.ksh.myapp.auth;

import com.ksh.myapp.auth.entity.Login;
import com.ksh.myapp.auth.entity.LoginRepository;
import com.ksh.myapp.auth.entity.Profile;
import com.ksh.myapp.auth.entity.ProfileRepository;
import com.ksh.myapp.auth.request.SignupRequest;
import com.ksh.myapp.auth.util.HashUtil;
import com.ksh.myapp.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController //HTTP메서드(GET,POST,PUT,DELETE) -> 메소드의 반환값은 HTTP 응답 본문(Body)에 직접 작성:ason형식
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
    //회원가입 요청 처리
    @PostMapping(value = "/signup")
    public ResponseEntity<Map<String,Object>> signUp(@RequestBody SignupRequest req) {
        Optional<Login> findByNickname = repo.findByNickname(req.getNickname());
        Optional<Login> findUserId = repo.findByUserId(req.getUserId());

        if(findByNickname.isPresent()) {
            Map<String,Object> res = new HashMap<>();
            res.put("message", "이미 존재하는 별명입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }
        if(findUserId.isPresent()) {
            Map<String,Object> res = new HashMap<>();
            res.put("message", "이미 존재하는 아이디입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }
        service.createIdentity(req);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "회원가입이 완료되었습니다.");
        response.put("nickname", req.getNickname()); // 닉네임 값 추가

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //3. (브라우저) 쿠키를 생성(도메인에 맞게)
    @PostMapping(value = "/signin")
    public ResponseEntity signIn(
            @RequestParam String userId,
            @RequestParam String password,
            HttpServletResponse res) {
        System.out.println(userId);
        System.out.println(password);
        // 1. userId, pw 인증 확인
        //   1.1 userId으로 login테이블에서 조회후 id, secret까지 조회
        Optional<Login> login = repo.findByUserId(userId);
        // userId에 매칭이 되는 레코드가 없는 상태
        if(!login.isPresent()) {

//             401 Unauthorized
//             클라이언트에서 대충 뭉뜨그려서 [인증정보가 잘못되었습니다.]
//             [사용자이름 또는 패스워드가 잘못되었습니다.]
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isVerified = hash.verifyHash(password, login.get().getSecret());
//        System.out.println("verified: " + isVerified);

        if(!isVerified) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder
                            .fromHttpUrl("http://localhost:5502/login.html?err=Unauthorized")
                            .build().toUri())
                    .build();
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "입력정보를 다시 확인하세요.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Login l = login.get();
        // 2. profile 정보를 조회하여 인증키 생성(JWT)
        Optional<Profile> profile = profileRepo.findByLogin_Id(l.getId());
        // 로그인정보와 프로필 정보가 제대로 연결 안됨.
        if(!profile.isPresent()) {
//            return ResponseEntity
//                    .status(HttpStatus.FOUND)
//                    .location(ServletUriComponentsBuilder
//                            .fromHttpUrl("http://localhost:5502?err=Conflict")
//                            .build().toUri())
//                    .build();

            // 409 conflict: 데이터 현재 상태가 안 맞음
            Map<String, Object> response = new HashMap<>();
            response.put("message", "해당 사용자 정보가 없습니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

       String token = jwt.createToken(
                l.getId(), l.getUserId(),
                profile.get().getNickname(),profile.get().getCompanyName(),profile.get().getCompanyAddress());
        System.out.println(token);

        // 3. cookie와 헤더를 생성한후 리다이렉트
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwt.TOKEN_TIMEOUT / 1000L)); // 만료시간
        cookie.setDomain("localhost"); // 쿠키를 사용할 수 있 도메인

        // 응답헤더에 쿠키 추가
        res.addCookie(cookie);

        // 웹 첫페이지로 리다이렉트
//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .location(ServletUriComponentsBuilder
//                        .fromHttpUrl("http://localhost:5502")
//                        .build().toUri())
//                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "로그인이 완료되었습니다.");
        response.put("token", token);
//        response.put("nickname", profile.get().getNickname());// 닉네임 값 추가

        return ResponseEntity.status(HttpStatus.OK).body(response);

//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .location(ServletUriComponentsBuilder
//                        .fromHttpUrl("http://localhost:5502")
//                        .build().toUri())
//                .build();
    }

   @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        // 클라이언트에서 전달받은 토큰을 무효화하거나 블랙리스트에 추가하는 작업 수행
        // 로그아웃 성공 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "로그아웃 되었습니다.");
       System.out.println(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    }

