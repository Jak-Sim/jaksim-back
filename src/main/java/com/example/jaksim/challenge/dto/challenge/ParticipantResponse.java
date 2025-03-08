package com.example.jaksim.challenge.dto.challenge;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ParticipantResponse {
    private UUID userId;
    private String userImage;
    private String userName;
    private int userPoint;

    // 각 유저별 포인트
    public ParticipantResponse(UUID userId, String userImage, String userName, int userPoint) {
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.userPoint = userPoint;
    }
}
