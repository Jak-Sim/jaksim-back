package com.example.jaksim.login.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SignUpResponse {
    private String nickname;
    private String social;
    private String socialUserId;
    private String email;
    private String accessToken;
    private String refreshToken;
    private UUID userId;
}
