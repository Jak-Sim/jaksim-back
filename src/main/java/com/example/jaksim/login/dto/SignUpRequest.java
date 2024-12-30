package com.example.jaksim.login.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
	private String nickname;
	private String social;
	private String userUniqueId;
}
