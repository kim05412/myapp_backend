package com.ksh.myapp.auth;

import com.ksh.myapp.auth.Auth;
import com.ksh.myapp.auth.AuthProfile;
import com.ksh.myapp.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor; // 컨트롤러의 요청 처리 이전에 호출

import java.lang.reflect.Method;

//클라이언트의 요청이 컨트롤러로 가기 전에 중간에서 수행되는 역할
// 1. HTTP 요청을 가로채어 -> 인증 필요한지 여부 확인.
// 인증 필요x-> true-> Authconntroller거치지 않고 요청에 대한 응답하는 controller로 요청 전달
// 2.인증이 필요한 경우에 토큰 인증 및 토큰 검증
//  true-> Authconntroller로 요청 전달
// false -> 요청 전달 안함고 종료.
@Component // 해당 클래스가 Spring의 관리 대상인 '빈(Bean)'임을 나타냄
//->등록->WebMvcConfig에 Bean주입 가능
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    JwtUtil jwt; //JWT(JSON Web Token)을 사용하여 사용자 인증을 처리
    @Override
    //메서드가 발생할 수 있는 모든 예외를 처리하지 않고, 그 예외를 메서드를 호출한 곳으로 던짐
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 현재 메서드가 HandlerMethod 클래스의 인스턴스이면->true
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler; // 객체 형변환 -> 현재 메서드(controllerMethod) 정보 접근
            Method method = handlerMethod.getMethod(); // 객체의 메서드 가져와서 할당

            // 현재 controller메서드에 해당하는 메서드에 @Auth 어노테이션이 있는지 확인 -> 인증이 필요한 메서드 인지 확인
            Auth auth = method.getAnnotation(Auth.class);

// ex)조회: 인증 필요X->토큰 관련 처리를 별도로 하지 않음
            if(auth == null) {
                return true; // controller로 요청 전달
            }
// ex) 작성: 인증이 필요한 경우 :로그인 해야지 쓸 수있는 기능에 대한 요청에 대한 응답처리
            // 2. 인증토큰 읽어오기
            // Request: Header에 authorization -> 토큰 값 읽어 옴
            String token = request.getHeader("Authorization"); // client 요청에 대한 토큰
            System.out.println(token);

            // ERROR: 인증 토큰이 없으면
            if(token == null || token.isEmpty()) {
                // 401: Unauthorized(미인가인데, 미인증이라는 의미로 사용)
                // 인증토큰이 없음 ->sol: js>fetch>header>Authorization: `Bearer ${getCookie(
                //            "token"확인
                response.setStatus(401);
                return false; // 컨트롤러로 요청 전달X->종료
            }

            // 3. 인증토큰 검증 및 페이로드(subject/claim) 객체화: 토큰에서 정보 추출
            // 페이로드(payload) : 토큰을 발급한 서버가 클라이언트에게 전달하고자 하는 사용자 정보와 기타 클레임(claim)이 포함
            AuthProfile profile =
                    // 토큰을 검증하고 디코딩
                    jwt.validateToken(token.replace("Bearer ", "")); //"Bearer " 접두사를 제거하여 순수한 토큰 문자열을 얻음-> 토큰 검증.
                    // -> JwtUtil: AuthProfile 정보 담은 객체 반환
            if(profile == null) {
                // 401: Unauthorized
                // 인증토큰이 잘못 됨
                response.setStatus(401);
                return false;
            }

            // 4. 검증 및 추출한 사용자 정보를-> 요청 속성(attribute)에 프로필 객체 추가하기->컨트롤러 메서드에서 해당 정보를 활용
            request.setAttribute("authProfile", profile);
            return true; // 인증이 필요한 기능 -> controller에 전달
        }

        return true;
    }
}
