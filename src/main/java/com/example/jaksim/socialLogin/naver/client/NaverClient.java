package com.example.jaksim.socialLogin.naver.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jaksim.socialLogin.config.FeignConfiguration;
import com.example.jaksim.socialLogin.naver.dto.NaverInfo;
import com.example.jaksim.socialLogin.naver.dto.NaverToken;

@FeignClient(name = "naverClient", configuration = FeignConfiguration.class)
public interface NaverClient {

	@PostMapping
	NaverToken getNaverToken(URI baseUrl, @RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String ClientId,
		@RequestParam("client_secret") String SecretKey,
		@RequestParam("code") String code,
		@RequestParam("state") String state
		);

	@PostMapping
	NaverInfo getNaverInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);
}