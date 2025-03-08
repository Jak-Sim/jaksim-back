package com.example.jaksim.challenge.dto.challenge;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChallengeJoinRequest {
    private UUID challengeId;
    private UUID userId;
}