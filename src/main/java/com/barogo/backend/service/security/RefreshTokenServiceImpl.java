package com.barogo.backend.service.security;

import com.barogo.backend.configuration.security.JwtTokenProvider;
import com.barogo.backend.dao.security.RefreshTokenDao;
import com.barogo.backend.domain.security.RefreshToken;
import com.barogo.backend.exception.JwtErrorCode;
import com.barogo.backend.exception.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenDao refreshTokenDao;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public RefreshToken save(String token) {
        LocalDateTime expiration = jwtTokenProvider.getExpirationFromJWT(token);
        String username = jwtTokenProvider.getUserIdFromJWT(token);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .expiryDate(expiration)
                .token(token)
                .build();

        refreshTokenDao.save(refreshToken);

        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> getByToken(String refreshToken) {
        return refreshTokenDao.findByToken(refreshToken);
    }

    @Transactional(noRollbackFor = JwtException.class)
    @Override
    public RefreshToken getVerifiedTokenByToken(String token) throws JwtException {
        Optional<RefreshToken> optionalRefreshToken = getByToken(token);
        RefreshToken refreshToken = optionalRefreshToken.orElseThrow(
                () -> new JwtException("Couldn't find RefreshToken", JwtErrorCode.ILLEGAL_ARGUMENT));

        if (refreshToken.isExpired()) {
            refreshTokenDao.delete(refreshToken);
            throw new JwtException(JwtErrorCode.EXPIRED.getMessage(), JwtErrorCode.EXPIRED);
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public void delete(String username) {
        RefreshToken findToken = refreshTokenDao.findById(username).orElseThrow(
                () -> new JwtException("Couldn't find RefreshToken", JwtErrorCode.ILLEGAL_ARGUMENT)
        );
        refreshTokenDao.delete(findToken);
    }
}
