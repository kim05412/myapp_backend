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
@Service
public class AuthService {

    private LoginRepository repo;
    private ProfileRepository profileRepo;

    @Autowired
    private HashUtil hash;

    @Autowired
    public AuthService(LoginRepository repo, ProfileRepository profileRepo) {
        this.repo = repo;
        this.profileRepo = profileRepo;}
    @Transactional
    public long createIdentity(SigngupRequest req) {
        // 1. login 정보를 insert
        Login toSaveLogin =
                Login.builder()
                        .username(req.getUsername())
                        .secret(hash.createHash(req.getPassword()))
                        .build();
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

