package com.barogo.backend.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"message"})
public enum JwtErrorCode {

	SIGNATURE("Invalid JWT signature"), MALFORMED("Invalid JWT token"),
	EXPIRED("Expired JWT token"), UNSUPPORTED("Unsupported JWT token"),
	ILLEGAL_ARGUMENT("JWT claims string is empty");

	private final String message;

	JwtErrorCode(String message) {
		this.message = message;
	}


}
