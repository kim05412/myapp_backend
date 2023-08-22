package com.ksh.myapp.auth.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashUtil {
    public String createHash(String cipherText) {
        // 주어진 문자열을 해시화
        return BCrypt
                .withDefaults()  // 해시생성에 필요한 기본 설정
                .hashToString(12, cipherText.toCharArray()); // 주어진 문자열->char배열 -> 해시화-> 반환
    }

    // ciphertext와 해시값을 비교하여 일치 여부를 확인
    public boolean verifyHash(String ciphertext, String hash) {
            return BCrypt
                    .verifyer() // 검증할 인스턴스 생성
                    .verify(ciphertext.toCharArray(), hash) //비교
                    .verified; //boolean 값 반환
        }
    }

