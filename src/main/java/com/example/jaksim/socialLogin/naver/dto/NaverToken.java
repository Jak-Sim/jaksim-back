package com.example.jaksim.socialLogin.naver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverToken {
	private String tokenType;
	private String accessToken;
	private String refreshToken;
	private Long expiresIn;

	public static NaverToken fail() {
		return new NaverToken(null, null);
	}

	private NaverToken(final String accessToken, final String refreshToken) {

		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}


}