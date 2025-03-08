package com.example.jaksim.login.entity;

import com.example.jaksim.user.entity.User;
import jakarta.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "login_id", columnDefinition = "binary(16)")
	private UUID loginId;

	@Column(name = "user_id", columnDefinition = "binary(16)", nullable = false)
	private UUID userId;

	@Column(nullable = false, unique = true)
	private String socialUserId;

	@Column(nullable = false, unique = true)
	private UUID tokenVersion;

	private LocalDateTime createdAt;

	private LocalDateTime lastLogin;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType social;

	public Login(UUID tokenVersion, String social, UUID userId, String socialUserId) {
		this.tokenVersion = tokenVersion;
		this.createdAt = LocalDateTime.now();
		this.social = SocialType.valueOf(social);
		this.userId = userId;
		this.socialUserId = socialUserId;
	}

	public static Login create(UUID tokenVersion, String social, UUID userId, String socialUserId) {
		return new Login(tokenVersion, social, userId, socialUserId);
	}

	// 로그아웃 시 토큰 버전 업데이트
	public void logout() {
		this.tokenVersion = UUID.randomUUID();
		this.lastLogin = LocalDateTime.now();
	}
}