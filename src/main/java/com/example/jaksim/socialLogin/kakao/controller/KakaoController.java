package com.example.jaksim.socialLogin.kakao.controller;

import com.example.jaksim.login.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jaksim.common.ResponseDto;
import com.example.jaksim.socialLogin.kakao.service.KakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoController {

	private final KakaoService kakaoService;
	/**
	 * callback을 통해 받은 code를 이용하여 token발급과, UserInfo를 받음
	 */
	@PostMapping("/sign-in/kakao")
	public ResponseEntity<ResponseDto> getKakaoAccount(@RequestBody LoginRequest requestDto,
													   final HttpServletResponse response) {
		return kakaoService.getKakaoInfo(requestDto, response);
	}

}