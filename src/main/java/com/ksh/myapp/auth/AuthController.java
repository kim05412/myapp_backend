package com.ksh.myapp.auth;

import com.ksh.myapp.auth.entity.Login;
import com.ksh.myapp.auth.entity.LoginRepository;
import com.ksh.myapp.auth.entity.Profile;
import com.ksh.myapp.auth.entity.ProfileRepository;
import com.ksh.myapp.auth.request.SignupRequest;
import com.ksh.myapp.auth.util.HashUtil;
import com.ksh.myapp.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Base64;
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
            @RequestParam("file") MultipartFile businessCard,
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
                .businessCard(businessCard)
                .build();

// 1.Validation(입력값 검증): interceptor -> 인증 필요한 요청 중 검증 완료 후-> controller로 전달
        // 2. Business Logic(DB데이터 처리):service->회원가입시, 로그인-프로필 객체 일관성(트랜잭션 처리)
        long profileId = service.createIdentity(createUser);
        System.out.println(createUser);

        // 3. Response
        // 201: created
        return ResponseEntity.status(HttpStatus.CREATED).body(profileId);

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
        Optional<Login> login = repo.findByUsername(userId);
        // username에 매칭이 되는 레코드가 없는 상태
        if(!login.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder
                            .fromHttpUrl("http://localhost:5502/index.html?err=Unauthorized")
                            .build().toUri())
                    .build();
            // 401 Unauthorized
            // 클라이언트에서 대충 뭉뜨그려서 [인증정보가 잘못되었습니다.]
            // [사용자이름 또는 패스워드가 잘못되었습니다.]
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //   1.2 password+salt -> 해시 -> secret 일치여부 확인
        //   401 Unauthorized 반환 종료
        boolean isVerified = hash.verifyHash(password, login.get().getSecret());
        System.out.println("verified: " + isVerified);
        if(!isVerified) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder
                            .fromHttpUrl("http://localhost:5502/index.html?err=Unauthorized")
                            .build().toUri())
                    .build();
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Login l = login.get();
        // 2. profile 정보를 조회하여 인증키 생성(JWT)
        Optional<Profile> profile = profileRepo.findByLogin_Id(l.getId());
        // 로그인정보와 프로필 정보가 일치 하지 않으면 -> error: 409 conflict
         if(!profile.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder
                            .fromHttpUrl("http://localhost:5502?err=Conflict")
                            .build().toUri())
                    .build();
            // 409 conflict: 데이터 현재 상태가 안 맞음
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String token = jwt.createToken(
                l.getId(), l.getUsername(),
                profile.get().getNickname());
        System.out.println(token);

        // 3. cookie와 헤더를 생성한후 리다이렉트
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwt.TOKEN_TIMEOUT / 1000L)); // 만료시간
        cookie.setDomain("localhost"); // 쿠키를 사용할 수 있 도메인

        // 응답헤더에 쿠키 추가
        res.addCookie(cookie);

        // 웹 첫페이지로 리다이렉트
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(ServletUriComponentsBuilder
                        .fromHttpUrl("http://localhost:5502")
                        .build().toUri())
                .build();
    }

}
