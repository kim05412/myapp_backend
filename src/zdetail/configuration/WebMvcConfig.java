package com.ksh.myapp.auth.configuration;
//웹 애플리케이션에서 CORS(Cross-Origin Resource Sharing)설정과
// 사용자 인증을 위한 인터셉터를 등록하는 설정 클래스
import com.ksh.myapp.auth.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// Spring MVC를 사용하도록 설정
@Configuration
// 클래스가 스프링 설정 클래스
@EnableWebMvc
//스프링 MVC 기능을 활성화
public class WebMvcConfig implements WebMvcConfigurer {
//    웹 애플리케이션에 관련된 설정을 수행, 커스터마이징 할 수 있는 메서드들을 추가 가능
    @Autowired
    // 의존성 주입
    private AuthInterceptor authInterceptor;
    //사용자 인증 처리를 위한 인터셉터


    @Override
    // CORS 설정 추가  및 인증 처리를 위한 인터셉터 등록 부분
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://127.0.0.1:5500") // 해당 도메인
        //모든 경로에 대해 해당 도메인에서 보내는 모든 종류의 요청(HTTP 메소드)만을 허용->처리
         // HTTP 요청 메소드(GET, POST, PUT, DELETE 등)
                .allowedMethods("*"); // 모든 메서드 허용(GET, POST.....)
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
}
