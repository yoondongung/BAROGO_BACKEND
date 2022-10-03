package com.barogo.backend.service;

import com.barogo.backend.domain.User;

public interface UserService {

    /**
     * ID 중복 체크, 패스워드 유효성 체크 후 저장됩니다.
     * @param user 유저 정보
     */
    void registerUser(User user);

    /**
     * USER 정보 조회
     * @param userId 유저 아이디
     * @return 있으면 USER 정보를 반환, 없으면 EXCEPTION 발생
     */
    User findUser(String userId);

    /**
     * 등록된 유저를 삭제
     * @param userId 유저 아이디
     */
    void deleteUser(String userId);

    /**
     * 비밀번호는 영어 대문자, 영어 소문자, 숫자, 특수문자 중 3종류 이상으로
     * 	12자리 이상의 문자열로 생성해야 합니다.
     * @param password 비밀번호
     */
    void isValidPassword(String password);
}
