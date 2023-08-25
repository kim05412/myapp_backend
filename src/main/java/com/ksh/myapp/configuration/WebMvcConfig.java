package com.ksh.myapp.configuration;
//웹 애플리케이션의 CORS 설정과 인증 처리용 인터셉터 설정을 구성
import com.ksh.myapp.auth.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //스프링 설정 파일의 역할-> bean
@EnableWebMvc // 스프링 MVC의 설정을 커스터마이즈
// 구현->웹 MVC의 구성을 커스터마이즈할 수 있는 메서드를 제공
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired // 인증 처리를 담당하는 클래스
    private AuthInterceptor authInterceptor;

    // CORS(cross orgin resource sharing ) 있게 하는 것

    // origin의 구성요소는 프로토콜+주소(도메인,IP)+포트
    // http:// + 127.0.0.1 + :5500
    // http://localhost:8080

    // 서버쪽에서 접근 가능한 orgin 목록, 경로목록, 메서드 목록을 설정해주어야함.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://127.0.0.1:5500",
                        "http://192.168.100.94:5500",
                        "http://localhost:5502",
                        "http://192.168.100.94:5502"
                ) // 특정 origin(호스트) 접근 허용
                .allowedMethods("*"); // 모든 HTTP 메서드 허용(GET, POST.....)
    }

    // 인증처리용 인터셉터를 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
    //인터셉터를 등록하는 역할
}