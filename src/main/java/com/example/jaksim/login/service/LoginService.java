package com.example.jaksim.login.service;

import com.example.jaksim.common.ResponseDto;
import com.example.jaksim.common.jwt.JwtUtil;
import com.example.jaksim.common.jwt.TokenDto;
import com.example.jaksim.login.dto.SignUpRequest;
import com.example.jaksim.login.dto.SignUpResponse;
import com.example.jaksim.login.entity.Login;
import com.example.jaksim.login.entity.SocialType;
import com.example.jaksim.login.repository.LoginRepository;
import com.example.jaksim.user.entity.User;
import com.example.jaksim.user.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;



@Service

public class LoginService {

	private final LoginRepository loginRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public LoginService(LoginRepository loginRepository, UserRepository userRepository, JwtUtil jwtUtil) {
		this.loginRepository = loginRepository;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil; // JwtUtil 초기화
	}

	public ResponseEntity<ResponseDto> signUp(SignUpRequest signUpRequest) {
		User user = new User();
		user.setUsername(signUpRequest.getNickname());
		user.setSocial(signUpRequest.getSocial());
		userRepository.save(user);

		UUID tokenVersion = UUID.randomUUID(); // 최초 토큰 버전 생성
		UUID userId = user.getUserId();
		Login login = Login.create(tokenVersion, signUpRequest.getSocial(), userId, signUpRequest.getSocialUserId()); // Login 객체 생성
		loginRepository.save(login); // DB에 저장

		Optional<Login> member = loginRepository.findBySocialUserIdAndSocial(login.getSocialUserId(), SocialType.KAKAO);
		TokenDto tokenDto = jwtUtil.createAllToken(String.valueOf(member.get().getUserId()), String.valueOf(member.get().getTokenVersion()));

		// SignUpResponse 객체 생성 및 데이터 설정
		SignUpResponse signUpResponse = new SignUpResponse();
		signUpResponse.setNickname(user.getUsername());
		signUpResponse.setSocial(signUpRequest.getSocial());
		signUpResponse.setSocialUserId(login.getSocialUserId());
		signUpResponse.setAccessToken(tokenDto.getAccessToken());
		signUpResponse.setRefreshToken(tokenDto.getRefreshToken());
		signUpResponse.setUserId(user.getUserId());

		return new ResponseEntity<>(new ResponseDto(209, "회원가입에 성공하였습니다.", signUpResponse), HttpStatus.OK);
	}

	public ResponseEntity<ResponseDto> reissue(String refreshToken) {
		if(jwtUtil.refreshTokenValidation(refreshToken)){
			String tokenVersion = jwtUtil.extractTokenVersion(refreshToken);
			Optional<Login> member = loginRepository.findByTokenVersion(UUID.fromString(tokenVersion));
			String accessToken = jwtUtil.createAccessToken(String.valueOf(member.get().getUserId()), "Access");

			Map<String, Object> responseData = new HashMap<>();
			responseData.put("accessToken",accessToken);
			responseData.put("refreshToken", refreshToken);

			return new ResponseEntity<>(new ResponseDto(209, "재발급에 성공하였습니다.", responseData), HttpStatus.OK);

		}
		else{
			return new ResponseEntity<>(new ResponseDto(424, "잘못된 token입니다."), HttpStatus.OK);
		}
	}
}
