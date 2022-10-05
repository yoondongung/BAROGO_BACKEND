package com.barogo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class JwtInfo {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class Request {
		private String refreshToken;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class Response {
		private String accessToken;
		private String refreshToken;
	}
}
