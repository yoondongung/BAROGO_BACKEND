package com.barogo.backend.service.login;

import com.barogo.backend.dto.JwtInfo;
import com.barogo.backend.dto.UserRequest;

public interface LoginService {

    /**
     * 유저 id, pw를 받아서 로그인 처리 후 토큰 발행
     * @param request
     * @return
     */
    JwtInfo.Response login(UserRequest request);
}
