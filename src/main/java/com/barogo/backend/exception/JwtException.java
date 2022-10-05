package com.barogo.backend.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = "code")
public class JwtException extends RuntimeException {

	private final JwtErrorCode code;

	public JwtException(String message, JwtErrorCode code) {
		super(message);
		this.code = code;
	}

	public JwtException(String message, JwtErrorCode code, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
}
