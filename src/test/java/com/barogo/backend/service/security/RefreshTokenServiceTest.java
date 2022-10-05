package com.barogo.backend.service.security;

import com.barogo.backend.configuration.security.JwtTokenProvider;
import com.barogo.backend.dao.security.RefreshTokenDao;
import com.barogo.backend.domain.security.RefreshToken;
import com.barogo.backend.exception.JwtErrorCode;
import com.barogo.backend.exception.JwtException;
import com.barogo.backend.service.user.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    RefreshTokenDao dao;

    @Spy
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    RefreshTokenServiceImpl service;

    @Test
    @DisplayName("토큰을 사용해서 저장")
    void testSave() {
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken("barogoUser1", "BarogoUser1!");
        String token = jwtTokenProvider.generateRefreshToken(authenticationToken);

        RefreshToken refreshToken = service.save(token);
        assertThat(refreshToken.getToken()).isEqualTo(token);
        assertThat(refreshToken.getUsername()).isEqualTo(authenticationToken.getPrincipal());
        assertThat(refreshToken.getExpiryDate()).isEqualTo(jwtTokenProvider.getExpirationFromJWT(token));
        verify(dao, Mockito.times(1)).save(refreshToken);
    }

    @Test
    void testGetByToken() {
        String refreshToken = "test_refreshToken";
        when(dao.findByToken(refreshToken))
                .thenReturn(Optional.of(RefreshToken.builder()
                        .username("barogoUser1")
                        .token(refreshToken)
                        .expiryDate(LocalDateTime.now())
                        .build()));
        Optional<RefreshToken> token = service.getByToken(refreshToken);
        assertThat(token.get().getToken()).isEqualTo(refreshToken);
    }

    @Test
    void testGetVerifiedTokenByToken() {
        String refreshToken = "test_refreshToken";
        when(dao.findByToken(refreshToken))
                .thenReturn(Optional.of(RefreshToken.builder()
                        .username("barogoUser1")
                        .token(refreshToken)
                        .expiryDate(LocalDateTime.now().plusDays(1))
                        .build()));

        RefreshToken token = service.getVerifiedTokenByToken(refreshToken);
        assertThat(token.getToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("토큰 조회 시, 만기된 토큰일 떄")
    void testGetVerifiedTokenByTokenExpiry() {
        String refreshToken = "test_refreshToken";
        when(dao.findByToken(refreshToken))
                .thenReturn(Optional.of(RefreshToken.builder()
                        .username("barogoUser1")
                        .token(refreshToken)
                        .expiryDate(LocalDateTime.now().minusDays(1))
                        .build()));
        JwtException jwtException = assertThrows(JwtException.class, () -> {
            service.getVerifiedTokenByToken(refreshToken);
        });
        assertThat(jwtException.getCode()).isEqualTo(JwtErrorCode.EXPIRED);
    }

    @Test
    @DisplayName("토큰 조회 시, 리턴값이 없을 떄")
    void testGetVerifiedTokenByTokenNotFound() {
        String refreshToken = "test_refreshToken";
        when(dao.findByToken(refreshToken))
                .thenReturn(Optional.empty());
        JwtException jwtException = assertThrows(JwtException.class, () -> {
            service.getVerifiedTokenByToken(refreshToken);
        });
        assertThat(jwtException.getCode()).isEqualTo(JwtErrorCode.ILLEGAL_ARGUMENT);
    }

    @Test
    void testDelete() {
        String username = "barogoUser1";
        String refreshToken = "test_refreshToken";
        when(dao.findById(username))
                .thenReturn(Optional.of(RefreshToken.builder()
                        .username("barogoUser1")
                        .token(refreshToken)
                        .expiryDate(LocalDateTime.now())
                        .build()));
        service.delete(username);
    }

    @Test
    @DisplayName("토큰 조회 시, 리턴값이 없을 떄")
    void testDeleteTokenNotFound() {
        String username = "barogoUser1";
        when(dao.findById(username))
                .thenReturn(Optional.empty());

        JwtException jwtException = assertThrows(JwtException.class, () -> {
            service.delete(username);
        });

        assertThat(jwtException.getCode()).isEqualTo(JwtErrorCode.ILLEGAL_ARGUMENT);
    }
}