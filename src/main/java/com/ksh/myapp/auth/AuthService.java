package com.ksh.myapp.auth;

import com.ksh.myapp.auth.entity.Login;
import com.ksh.myapp.auth.entity.LoginRepository;
import com.ksh.myapp.auth.entity.Profile;
import com.ksh.myapp.auth.entity.ProfileRepository;
import com.ksh.myapp.auth.request.SigngupRequest;
import com.ksh.myapp.auth.util.HashUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 로그인 데이터 정보, 프로필 데이터 정보 받아 옴
@Service  //컨트롤러와 리포지토리 사이의 중간 계층으로 동작
public class AuthService {

    private LoginRepository repo;
    private ProfileRepository profileRepo; //db

    @Autowired
    private HashUtil hash; // 해시화,일치여부 검증

    @Autowired
    public AuthService(LoginRepository repo, ProfileRepository profileRepo) {
        this.repo = repo;
        this.profileRepo = profileRepo;
    }

    //새로운 사용자의 신원 정보를 생성
    @Transactional // 회원가입시 로그인정보와 프로필 정보 동시에 처리->데이터 일관성 유지
    public long createIdentity(SigngupRequest req) {
        // 1. login 정보를 insert
        Login toSaveLogin =
                Login.builder()
                        .username(req.getUsername())
                        .secret(hash.createHash(req.getPassword())) // 비밀번호 해싱
                        .build(); // 해싱한 비번 결과 반환
        Login savedLogin = repo.save(toSaveLogin);

        // 2. profile 정보를 insert(login_id포함)하고 레코드의 id값을 가져옴;
        Profile toSaveProfile =
                Profile.builder()
                        .nickname(req.getNickname())
                        .login(savedLogin)
                        .build();
        long profileId = profileRepo.save(toSaveProfile).getId();

        // 3. 로그인 정보에는 profile_id값만 저장
        savedLogin.setProfileId(profileId);
        repo.save(savedLogin);

        // 4. profile_id를 반환
        return profileId;
    }


}