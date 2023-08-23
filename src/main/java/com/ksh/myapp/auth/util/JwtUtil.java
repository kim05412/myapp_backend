package com.ksh.myapp.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ksh.myapp.auth.AuthProfile;

import java.util.Date;

// 토큰
public class JwtUtil {
    public String secret = "your-secret";
    //365일 : ms 단위
    public final long TOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 7;

    //JSON Web Token(JWT) 생성
    // claim(이름,값): 토큰에 포함되는 정보-> 토큰에 사용자 식별정보/추가 데이터 담음
    public String createToken(long id, String username, String nickname) {
        // 토큰 생성시간
        Date now = new Date();
        //만료시간
        Date exp = new Date(now.getTime() + TOKEN_TIMEOUT);
        // JWT의 서명 알고리즘을 설정
        Algorithm algorithm = Algorithm.HMAC256(secret); // 비밀키을 해싱->토큰서명에 비밀키 사용하는 알고리즘 생성

        return JWT.create() // 빌더 객체 생성 하고 반환
                .withSubject(String.valueOf(id)) // 토큰의 소유자(sub)설정 <-회원 가입시 자동 생성
                // with: claim 생성->추가
                .withClaim("username", username) //식별정보 추가
                .withClaim("nickname", nickname) //식별정보 추가
                //토큰 유효성 관리
                .withIssuedAt(now) //언제부터 유효
                .withExpiresAt(exp) //언제까지 유효
                // 비밀키 alrorithm을 이용해 토큰을 서명 -> 무결성 보장->토큰 검증에 사용
                .sign(algorithm);
    } //생성된 JWT는 클라이언트에게 제공->인증 필요한 기능에 토큰 사용
    //AuthInterceptor에서 토큰 검증(토큰의 서명을 생성한 비밀 키 필요)하는데 사용 됨.


    // JWT 토큰에서 추출한 정보를 사용 -> AuthProfile 객체를 생성
    // interceptor에서 토큰 값 검증에 사용
    public AuthProfile validateToken(String token) {
        // JWT 토큰에서 정보 추출에 사용될 알고리즘 생성
        Algorithm algorithm = Algorithm.HMAC256(secret); // 비밀키: createToken과 같은 값
        // 요구된 특정 알고리즘 사용하여 검증하고 디코딩 할 verify 생성
        JWTVerifier verifier = JWT.require(algorithm).build(); // 토큰의 검증에 필요한 특정 알고리즘이 설정된 빌더 객체 생성

        try {
// 검증된 JWT 토큰에서 디코딩된 정보 추출
            // verify: 토큰의 검증 + 토큰에 담긴 정보 디코딩
            DecodedJWT decodedJWT = verifier.verify(token); // 검증된 토큰의 정보를 디코딩
            Long id = Long.valueOf(decodedJWT.getSubject()); //사용자의 식별 정보(id) 가져옴-> id값 추출-> Long 형변환
            String nickname = decodedJWT
                    .getClaim("nickname").asString();
            String username = decodedJWT
                    .getClaim("username").asString();

//해당 사용자의 일부 정보 가져와서 AuthProfile 객체를 생성
            return AuthProfile.builder() //빌더 객체 생성
                    .id(id)
                    .username(username)
                    .nickname(nickname)
                    .build(); // 추출한 정보 담은 객체 반환

// 토큰 검증이 실패한 경우에 실행
        } catch (JWTVerificationException e) {
//            토큰이 유효하지 않거나 토큰 검증에 오류가 발생한 경우에는 null을 반환
            return null;
        }
    }

}
