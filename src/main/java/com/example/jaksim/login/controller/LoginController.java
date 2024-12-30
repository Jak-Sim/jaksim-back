package com.example.jaksim.login.controller;

import com.example.jaksim.common.ResponseDto;
import com.example.jaksim.login.dto.SignUpRequest;
import com.example.jaksim.login.service.LoginService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping("/sign-up")
	public ResponseEntity<ResponseDto> signUp(@RequestBody SignUpRequest signUpRequest) {
			return loginService.signUp(signUpRequest);

	}

	@PostMapping("/refresh-token")
	public ResponseEntity<ResponseDto> reissue(@RequestBody String RT) {
		System.out.println(RT);
		return loginService.reissue(RT);

	}
}
