package com.barogo.backend.domain.security;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "BAROGO_REFRESH_TOKEN")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	private String username;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false, updatable = true)
	private LocalDateTime expiryDate;

	@Builder
	public RefreshToken(String username, String token, LocalDateTime expiryDate) {
		this.username = username;
		this.token = token;
		this.expiryDate = expiryDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RefreshToken that = (RefreshToken) o;

		return username.equals(that.username);
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	public boolean isExpired() {
		return getExpiryDate().isBefore(LocalDateTime.now());
	}
}
