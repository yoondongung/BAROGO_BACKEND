package com.barogo.backend.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

/**
 * 토큰을 생성하고 검증하는 클래스
 * 해당 클래스는 필터클래스에서 사전 검증을 거친다.
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "barogoBackEndTest";

    // 토큰 유효시간 10분
    private static final long EXPIRATION_MS = 10 * 60 * 1000L;

    // 토큰 갱신 시간 30분
    private static final long REFRESH_EXPIRATION_MS = 30 * 60 * 1000L;

    private static final String BEARER = "Bearer ";

    /**
     * 객체 초기화, secretKey를 Base64로 인코딩한다.
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Access 토큰 생성
     *
     * @param authentication Authentication
     * @return Access 토큰
     */
    public String generateAccessToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        return token((String) authentication.getPrincipal(), expiryDate);
    }

    /**
     * Refresh 토큰 생성
     *
     * @param authentication Authentication
     * @return Refresh 토큰
     */
    public String generateRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_EXPIRATION_MS);
        return token((String) authentication.getPrincipal(), expiryDate);
    }

    /**
     * Access 토큰과 Refresh 토큰을 생성
     *
     * @param authentication Authentication
     * @return Pair<String, String> access 토큰, refresh 토큰
     */
    public Pair<String, String> generateAccessAndRefreshToken(Authentication authentication) {
        return Pair.of(generateAccessToken(authentication), generateRefreshToken(authentication));
    }

    private String token(String userId, Date expiryDate) {
        return Jwts.builder()
                .setSubject(userId) // 사용자 payload
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(expiryDate) // 만료 시간 세팅
                .signWith(SignatureAlgorithm.HS512, secretKey) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();
    }

    /**
     * UserId를 확인하는 메소드
     *
     * @param token JWT 토큰
     * @return User 아이디
     */
    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 토큰 만료일자를 확인 하는 메소드
     *
     * @param token JWT 토큰
     * @return 만기일
     */
    public LocalDateTime getExpirationFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();

        return expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 토큰의 유효성 , 만료일자 확인
     *
     * @param token JWT 토큰
     * @return 유효한지 아닌지 true/false
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * REQUEST 정보에서 JWT TOKEN 반환
     *
     * @param request 요청 정보
     * @return JWT 토큰
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    /**
     * Test 용으로 토큰 생성
     *
     * @param userId 유저 id
     * @return JWT 토큰
     */
    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        return token(userId, expiryDate);
    }
}
