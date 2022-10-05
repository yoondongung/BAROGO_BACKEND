package com.barogo.backend.service.login;

import com.barogo.backend.configuration.security.JwtTokenProvider;
import com.barogo.backend.dto.JwtInfo;
import com.barogo.backend.dto.UserRequest;
import com.barogo.backend.service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtInfo.Response login(UserRequest request) {
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUserId(), request.getPassword()
                        )
                );

        User user = (User) authenticate.getPrincipal();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
                new ArrayList<>(user.getAuthorities()));
        Pair<String, String> accessAndRefreshToken = tokenProvider.generateAccessAndRefreshToken(authentication);
        refreshTokenService.save(accessAndRefreshToken.getRight());

        return JwtInfo.Response.builder()
                .accessToken(accessAndRefreshToken.getLeft())
                .refreshToken(accessAndRefreshToken.getRight())
                .build();
    }
}
