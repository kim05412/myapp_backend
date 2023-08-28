package com.ksh.myapp.auth;

import com.ksh.myapp.auth.entity.Login;
import com.ksh.myapp.auth.entity.LoginRepository;
import com.ksh.myapp.auth.entity.Profile;
import com.ksh.myapp.auth.entity.ProfileRepository;
import com.ksh.myapp.auth.request.SignupRequest;
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

    //DB에 새로운 사용자의 신원 정보를 생성-> 로그인시: 회원가입시 생성한 프로필 정보와 로그인 정보의 연결을 통해 -> 현재 입력한 정보와의 일치여부 검증
    @Transactional // 회원가입시 로그인정보와 프로필 정보 동시에 처리->데이터 일관성 유지
    public long createIdentity(SignupRequest req) {
        // 1. new login 정보를 insert
        // 로그인 새 정보를 위한 객체 생성
        Login toSaveLogin =
                Login.builder() //빌더 패턴 -> 순서X
                        .username(req.getUserId()) //입력한 이름 가져옴
                        .secret(hash.createHash(req.getPassword())) // 입력한 비밀번호 해싱해서 저장
                        .build(); // 해싱한 비번 결과 반환
        Login savedLogin = repo.save(toSaveLogin); // 로그인을 위한 데이터 DB에 저장->저장된 값 받음

        // 2. new profile 정보를 insert(login_id포함)하고 레코드의 id값을 가져옴;
        Profile toSaveProfile = // 새 프로필을 위한 객체 생성
                Profile.builder() //빌더 패턴 사용
                        .nickname(req.getNickname()) //사용자가 입력한 닉네임 가져와서 저장
                        .login(savedLogin) // 연결할 로그인 정보(이전단계에서 저장된 로그인 정보-일관성 유지) 가져와서 저장
                        .build(); //생성한거 반환
        long profileId = profileRepo.save(toSaveProfile).getId(); // 프로필 정보를 db에 저장->프로필 정보 중 id값만 추출-> 할당

        // 3. 로그인 정보에는 profile_id값만 저장
        savedLogin.setProfileId(profileId); // 이전의 생성된 login정보 객체에 profile의 id 추가하고 저장 => 연관성 발생=>로그인시 프로필 참조 가능
        repo.save(savedLogin); // 변경 login정보를 da에 다시 저장

        // 4. 생성된 profile정보의 id를 반환
        return profileId; //-> ID는 이후에 프로필 정보를 참조하거나 연결된 작업에 사용
    }


}