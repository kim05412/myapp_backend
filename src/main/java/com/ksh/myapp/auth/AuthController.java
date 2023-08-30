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
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @PostMapping(value = "/signup")
    public ResponseEntity signUp(
            @RequestParam("file") String file,
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam("nickname") String nickname,
            @RequestParam("year") Long year,
            @RequestParam("companyName") String companyName,
            @RequestParam("companyAddress") String companyAddress) {

        // jason 아닌 form 형식 데이터 받기
        SignupRequest createUser = SignupRequest.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .year(year)
                .companyName(companyName)
                .companyAddress(companyAddress)
                .file(file)
                .build();

// 1.Validation(입력값 검증): interceptor -> 인증 필요한 요청 중 검증 완료 후-> controller로 전달
        // 2. Business Logic(DB데이터 처리):service->회원가입시, 로그인-프로필 객체 일관성(트랜잭션 처리)
        long profileId = service.createIdentity(createUser);
        System.out.println(createUser);

        // 3. Response
        // 201: created
//        return ResponseEntity.status(HttpStatus.CREATED).body(profileId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "회원가입이 완료되었습니다.");
        response.put("nickname", nickname); // 닉네임을 응답 데이터에 추가

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
            // 1. username, pw 인증 확인
            //   1.1 username으로 login테이블에서 조회후 id, secret까지 조회
            Optional<Login> login = repo.findByUserId(userId);
            // username에 매칭이 되는 레코드가 없는 상태
            if (!login.isPresent()) {
//
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            //   1.2 password+salt -> 해시 -> secret 일치여부 확인
            //   401 Unauthorized 반환 종료
            boolean isVerified = hash.verifyHash(password, login.get().getSecret());
            System.out.println("verified: " + isVerified);
            if (!isVerified) {
//
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Login l = login.get();
            // 2. profile 정보를 조회하여 인증키 생성(JWT)
            Optional<Profile> profile = profileRepo.findByLogin_Id(l.getId());
            // 로그인정보와 프로필 정보가 일치 하지 않으면 -> error: 409 conflict
            if (!profile.isPresent()) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            String token = jwt.createToken(
                    l.getId(), l.getUserId(),
                    profile.get().getNickname());
            System.out.println(token);

            // 3. cookie와 헤더를 생성
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwt.TOKEN_TIMEOUT / 1000L)); // 만료시간
            cookie.setDomain("localhost"); // 쿠키를 사용할 수 있 도메인

            // 응답헤더에 쿠키 추가
            res.addCookie(cookie);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "로그인이 완료되었습니다.");
            response.put("nickname", profile.get().getNickname()); // 닉네임 값 추가

            return ResponseEntity.status(HttpStatus.OK).body(response);
//        return ResponseEntity.ok("Form submitted successfully");
//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .location(ServletUriComponentsBuilder
//                        .fromHttpUrl("http://localhost:5500")
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
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    }

