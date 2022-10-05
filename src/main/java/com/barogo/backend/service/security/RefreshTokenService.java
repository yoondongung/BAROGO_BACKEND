package com.barogo.backend.service.security;

import com.barogo.backend.domain.security.RefreshToken;
import com.barogo.backend.exception.JwtException;

import java.util.Optional;

public interface RefreshTokenService {

    /**
     * Access 토큰을 이용해 refresh 토큰을 만들고  refresh 저장
     * @param token Access 토큰
     * @return refresh 토큰
     */
    RefreshToken save(String token);

    /**
     * refresh 토큰 조회
     * @param refreshToken refresh 토큰
     * @return refresh 토큰
     */
    Optional<RefreshToken> getByToken(String refreshToken);

    /**
     * refresh 토큰 유효성 검사, 만기 토큰을 경우 삭제 처리
     * @param refreshToken refresh 토큰
     * @return refresh 토큰
     * @throws JwtException Expired JWT token
     */
    RefreshToken getVerifiedTokenByToken(String refreshToken) throws JwtException;

    void delete(String username);
}
