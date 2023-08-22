package com.ksh.myapp.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ksh.myapp.auth.AuthProfile;

import java.util.Date;

public class JwtUtil {
    public String secret = "your-secret";
    //365일 : ms 단위
    public final long TOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 7;

    //JSON Web Token(JWT) 생성
    public String createToken(long id, String username, String nickname) {
        // 토큰 생성시간
        Date now = new Date();
        //만료시간
        Date exp = new Date(now.getTime() + TOKEN_TIMEOUT);

        Algorithm algorithm = Algorithm.HMAC256(secret); // JWT의 서명 알고리즘을 설정
        return JWT.create() // 빌더 객체 생성 -> 순서 없이 데이터 넣을 수O
                .withSubject(String.valueOf(id)) //토큰의 소유자(sub)설정
                .withClaim("username", username) //(이름,값)->클레임 추가
                .withClaim("nickname", nickname)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(algorithm);
    } //생성된 JWT는 클라이언트에게 전달되어 인증 및 정보 전달에 사용

    // 유효한 토큰인 경우에 해당 토큰의 내용을 추출하여 AuthProfile 객체를 생성
    public AuthProfile validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret); // 비밀키:토큰 생성시와 같은 값
        // algorith을 사용하여 검증하는 객체 생성
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token); //실행되면:토큰유효->값 담김
            Long id = Long.valueOf(decodedJWT.getSubject()); //토큰에서 id값 추출
            String nickname = decodedJWT
                    .getClaim("nickname").asString();
            String username = decodedJWT
                    .getClaim("username").asString();

            return AuthProfile.builder() //빌더 객체 생성
                    .id(id)
                    .username(username)
                    .nickname(nickname)
                    .build(); // 출추한 정보 담은 객체 반환

// 토큰 검증이 실패한 경우에 실행
        } catch (JWTVerificationException e) {
//            토큰이 유효하지 않거나 토큰 검증에 오류가 발생한 경우에는 null을 반환
            return null;
        }
    }

}
