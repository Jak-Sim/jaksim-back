package com.example.jaksim.socialLogin.kakao.client;


import java.net.URI;

import org.springframework.
	cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jaksim.socialLogin.config.FeignConfiguration;
import com.example.jaksim.socialLogin.kakao.dto.KakaoInfo;
import com.example.jaksim.socialLogin.kakao.dto.KakaoToken;

@FeignClient(name = "kakaoClient", configuration = FeignConfiguration.class)
public interface KakaoClient {

	@PostMapping
	KakaoToken getKakaoToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
		@RequestParam("redirect_uri") String redirectUrl,
		@RequestParam("code") String code,
		@RequestParam("grant_type") String grantType);

	@PostMapping
	String getKakaoInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);
}