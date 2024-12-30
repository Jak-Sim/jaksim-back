package com.example.jaksim.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jaksim.user.dto.UsernameCheckRequest;
import com.example.jaksim.user.service.UserService;

@RestController
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("sign-up/nick-check")
	public ResponseEntity<String> checkUsername(@RequestBody UsernameCheckRequest request) {
		boolean isTaken = userService.isUsernameTaken(request.getUsername());
		if (isTaken) {
			return ResponseEntity.ok("이미 존재하는 닉네임 입니다.");
		} else {
			return ResponseEntity.ok("사용가능한 닉네임 입니다.");
		}
	}

	// @GetMapping("")
	// public 
}
