package com.example.jaksim.login.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Login {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long memberId;

	@Column(nullable = false, unique = true)
	private String memberUniqueId;


	@Column(nullable = false, unique = true)
	private UUID tokenVersion;

	@Column(nullable = false, unique = true)
	private UUID userUuid;

	private LocalDateTime createdAt;

	private LocalDateTime lastLogin;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType social;

	// 생성자는 tokenVersion을 외부에서 받도록 수정
	public Login(String memberUniqueId, UUID tokenVersion, String social) {
		this.memberUniqueId = memberUniqueId;
		this.tokenVersion = tokenVersion; // 외부에서 전달받은 tokenVersion
		this.createdAt = LocalDateTime.now(); // 생성 시간 설정
		this.social = SocialType.valueOf(social);
	}

	public static Login create(String memberUniqueId, UUID tokenVersion, String social) {
		return new Login(memberUniqueId, tokenVersion, social);
	}

	// 로그아웃 시 토큰 버전 업데이트
	public void logout() {
		this.tokenVersion = UUID.randomUUID(); // 로그아웃 시 새로운 UUID로 토큰 버전 업데이트
		this.lastLogin = LocalDateTime.now(); // 마지막 로그인 시간 업데이트
	}
}
