package com.example.jaksim.login.service;

import com.example.jaksim.common.ResponseDto;
import com.example.jaksim.common.jwt.JwtUtil;
import com.example.jaksim.common.jwt.TokenDto;
import com.example.jaksim.login.dto.SignUpRequest;
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

		UUID tokenVersion = UUID.randomUUID(); // 최초 토큰 버전 생성
		UUID userUuid = UUID.randomUUID();
		Login login = Login.create(signUpRequest.getUserUniqueId(), tokenVersion, signUpRequest.getSocial()); // Login 객체 생성
		login.setUserUuid(userUuid);
		loginRepository.save(login); // DB에 저장

		user.setUserUuid(userUuid);
		user.setSocial(signUpRequest.getSocial());
		userRepository.save(user);

		Optional<Login> member = loginRepository.findByMemberUniqueIdAndSocial(String.valueOf(login.getMemberUniqueId()), SocialType.KAKAO);
		TokenDto tokenDto = jwtUtil.createAllToken(String.valueOf(member.get().getUserUuid()), String.valueOf(member.get().getTokenVersion()));

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("AT",tokenDto.getAccessToken());
		responseData.put("RT",tokenDto.getRefreshToken());
		responseData.put("nickname", user.getUsername());
		responseData.put("social", signUpRequest.getSocial());

		return new ResponseEntity<>(new ResponseDto(209, "로그인에 성공하셨습니다.", responseData), HttpStatus.OK);
	}

	public ResponseEntity<ResponseDto> reissue(String refreshToken) {
		if(jwtUtil.refreshTokenValidation(refreshToken)){
			String tokenVersion = jwtUtil.extractTokenVersion(refreshToken);
			Optional<Login> member = loginRepository.findByTokenVersion(UUID.fromString(tokenVersion));
			String accessToken = jwtUtil.createAccessToken(String.valueOf(member.get().getUserUuid()), "Access");

			Map<String, Object> responseData = new HashMap<>();
			responseData.put("AT",accessToken);
			responseData.put("RT", refreshToken);

			return new ResponseEntity<>(new ResponseDto(209, "재발급에 성공하셨습니다.", responseData), HttpStatus.OK);

		}
		else{
			return new ResponseEntity<>(new ResponseDto(424, "잘못된 Refresh Token 입니다."), HttpStatus.OK);
		}
	}
}
