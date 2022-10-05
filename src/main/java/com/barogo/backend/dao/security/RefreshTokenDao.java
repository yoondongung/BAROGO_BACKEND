package com.barogo.backend.dao.security;

import com.barogo.backend.domain.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenDao extends JpaRepository<RefreshToken, String> {

	Optional<RefreshToken> findByToken(String refreshToken);
}
