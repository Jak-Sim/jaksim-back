package com.example.jaksim.user.controller;

import com.example.jaksim.common.annotation.AuthUser;
import com.example.jaksim.user.dto.UserRecommendResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.jaksim.user.dto.UserUpdateDto;
import com.example.jaksim.user.dto.UsernameCheckRequest;
import com.example.jaksim.user.service.UserService;
import com.example.jaksim.user.entity.User;

import java.io.IOException;
import java.util.UUID;


@RestController
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "닉네임 체크")
	@PostMapping("sign-up/nick-check")
	public ResponseEntity<String> checkUsername(@RequestBody UsernameCheckRequest request) {
		boolean isTaken = userService.isUsernameTaken(request.getUsername());
		if (isTaken) {
			return ResponseEntity.ok("이미 존재하는 닉네임입니다.");
		} else {
			return ResponseEntity.ok("사용가능한 닉네임입니다.");
		}
	}

	@Operation(summary = "유저 데이터 업데이트", description = "유저 정보를 업데이트합니다.")
	@PutMapping("/user/update")
	public ResponseEntity<User> updateUser(
			@AuthUser UUID userId,
			@ModelAttribute UserUpdateDto userUpdateDto,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        User updatedUser = userService.updateUser(userId, userUpdateDto, profileImage);
        return ResponseEntity.ok(updatedUser);
    }

	@Operation(summary = "유저와 같은 챌린지 멤버 리스트", description = "for chat server")
	@GetMapping("/user/related")
	public ResponseEntity<UserRecommendResponse> getRelatedUsers(@AuthUser UUID userId) {
		UserRecommendResponse response = userService.findRelatedUsers(userId);
		return ResponseEntity.ok(response);
	}
}
