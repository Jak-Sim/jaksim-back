package com.example.jaksim.common.security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long refreshTokenId;
	@NotBlank
	private String refreshToken;
	@NotBlank
	private String memberUniqueId;

	public RefreshToken(String refreshToken, String memberUniqueId){
		this.refreshToken = refreshToken;
		this.memberUniqueId = memberUniqueId;
	}

	public RefreshToken updateToken(String refreshToken){
		this.refreshToken = refreshToken;
		return this;
	}
}
