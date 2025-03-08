package com.example.jaksim.login.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SignUpRequest {
	private String nickname;
	private String social;
	private String socialUserId;
}
