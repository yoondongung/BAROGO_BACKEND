package com.barogo.backend.service.security;

import com.barogo.backend.configuration.security.JwtTokenProvider;
import com.barogo.backend.service.user.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = JwtTokenProvider.class)
public class JwtTokenProviderTest {
    private static final long EXPIRATION_MS = 10 * 60 * 1000L;
    private static final long REFRESH_EXPIRATION_MS = 30 * 60 * 1000L;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("Access 토큰과 Refresh 토큰 생성")
    void testGenerateAccessAndRefreshToken() {

        String userId = "barogoUser1";
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

        Pair<String, String> token = jwtTokenProvider.generateAccessAndRefreshToken(authentication);

        String access = jwtTokenProvider.getUserIdFromJWT(token.getLeft());
        String refresh = jwtTokenProvider.getUserIdFromJWT(token.getRight());
        assertThat(access).isEqualTo(userId);
        assertThat(refresh).isEqualTo(userId);
    }

    @Test
    @DisplayName("Access 토큰 생성")
    void testGenerateToken() {
        String userId = "barogoUser1";
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

        String token = jwtTokenProvider.generateAccessToken(authentication);

        String userIdFromJWT = jwtTokenProvider.getUserIdFromJWT(token);
        assertThat(userIdFromJWT).isEqualTo(userId);
    }

    @Test
    @DisplayName("Refresh 토큰 생성")
    void testGenerateRefreshToken() {
        String userId = "barogoUser1";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userId);

        String jwtToken = jwtTokenProvider.generateRefreshToken(authentication);

        LocalDateTime expirationFromJWT = jwtTokenProvider.getExpirationFromJWT(jwtToken);
        long actual_expired_time = expirationFromJWT.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_EXPIRATION_MS);

        /**
         *  refresh 토큰 만기일은 현재시간 기준으로 + JwtTokenProvider.REFRESH_EXPIRATION_MS 로 생성
         *  유효성 검증은 ( 토큰 만기일은 - 10분으로 확인);
         */
        assertThat(actual_expired_time).isGreaterThan(expiryDate.getTime() - 600000L);
    }

    @Test
    @DisplayName("토큰에서 UserId 확인")
    void testGetUserIdFromJWT() {
        String userId = "barogoUser1";
        String jwtToken = jwtTokenProvider.generateToken(userId);

        String userIdFromJWT = jwtTokenProvider.getUserIdFromJWT(jwtToken);

        assertThat(userIdFromJWT).isEqualTo(userId);
    }

    @Test
    @DisplayName("토큰에서 만기일 체크")
    void testGetExpirationFromJWT() {
        String userId = "barogoUser1";
        String jwtToken = jwtTokenProvider.generateToken(userId);
        LocalDateTime expirationFromJWT = jwtTokenProvider.getExpirationFromJWT(jwtToken);

        long actual_expired_time = expirationFromJWT.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        /**
         *  access 토큰 만기일은 현재시간 기준으로 + JwtTokenProvider.REFRESH_EXPIRATION_MS 로 생성
         *  유효성 검증은 ( 토큰 만기일은 - 10분으로 확인);
         */
        assertThat(actual_expired_time).isGreaterThan(expiryDate.getTime() - 600000);
    }

    @Test
    void testValidateToken() {
        String jwtToken = jwtTokenProvider.generateToken("barogoUser1");
        boolean releaseSuccess = jwtTokenProvider.validateToken(jwtToken);
        assertThat(releaseSuccess).isTrue();
    }

    @Test
    void validateTokenError() {
        String differentSignatureToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
                ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        boolean isDifferentSignatureTokenValidate = jwtTokenProvider.validateToken(differentSignatureToken);
        Assertions.assertThat(isDifferentSignatureTokenValidate).isFalse();


        String falseFormatToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        boolean isFalseFormatTokenValidate = jwtTokenProvider.validateToken(falseFormatToken);
        Assertions.assertThat(isFalseFormatTokenValidate).isFalse();

        String expiredToken = "eyJhbGciOiJIUzUxMiJ9." +
                "eyJzdWIiOiJnb25naG9qaW4iLCJpYXQiOjE2NjM2NTA4MTUsImV4cCI6MTY1NzkzMTMzNX0" +
                ".feoS024dI9UZr4T9PaRfHauhBe0d87CrKAMKAguH3vyyt1IKyEd1NEbX3tGLi8_aJ_F4pys6n0_y8-aSFEBmSg";
        boolean isExpiredTokenValidate = jwtTokenProvider.validateToken(expiredToken);
        Assertions.assertThat(isExpiredTokenValidate).isFalse();
    }

    @Test
    void testGetJwtFromRequest() {
        HttpServletRequest mock = mock(HttpServletRequest.class);
        String jwt = "Test_JWT";
        when(mock.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + jwt);

        String findJWT = jwtTokenProvider.getJwtFromRequest(mock);
        assertThat(findJWT).isEqualTo(jwt);
    }

    @Test
    @DisplayName("Prefix에 Bearer이 없을 경우 null로 리턴")
    void testGetJwtFromRequestError1() {
        HttpServletRequest mock = mock(HttpServletRequest.class);
        String jwt = "Test_JWT";
        when(mock.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(jwt);

        String findJWT = jwtTokenProvider.getJwtFromRequest(mock);

        assertThat(findJWT).isNull();
    }

    @Test
    @DisplayName("Token이 비어있을 경우 null")
    void testGetJwtFromRequestError2() {
        HttpServletRequest mock = mock(HttpServletRequest.class);
        when(mock.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");

        String findJWT = jwtTokenProvider.getJwtFromRequest(mock);

        assertThat(findJWT).isNull();
    }
}
