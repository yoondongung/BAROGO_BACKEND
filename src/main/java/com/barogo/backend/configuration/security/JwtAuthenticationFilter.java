package com.barogo.backend.configuration.security;

import com.barogo.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwtTokenProvider가 검증이 끝낸 JWT 토큰으로 부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter로 전달하는 클래스
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtFromRequest = jwtTokenProvider.getJwtFromRequest((HttpServletRequest) request);
        if (StringUtils.isNotEmpty(jwtFromRequest) && jwtTokenProvider.validateToken(jwtFromRequest)) {

            Authentication authentication = getAuthentication(jwtFromRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(String token) {
        UserDetails details = userService.loadUserByUsername(jwtTokenProvider.getUserIdFromJWT(token));
        return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
    }
}
