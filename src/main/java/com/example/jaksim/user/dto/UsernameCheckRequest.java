package com.example.jaksim.user.dto;

public class UsernameCheckRequest {
	private String nickname;

	// 기본 생성자와 getter, setter
	public UsernameCheckRequest() {
	}

	public String getUsername() {
		return nickname;
	}

	public void setUsername(String nickname) {
		this.nickname = nickname;
	}
}
