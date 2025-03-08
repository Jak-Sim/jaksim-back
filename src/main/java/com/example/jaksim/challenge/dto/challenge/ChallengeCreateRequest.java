package com.example.jaksim.challenge.dto.challenge;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChallengeCreateRequest {
	private String name;
	private String backgroundImage;
	private boolean isPublic;
	private int minParticipants;
	private int maxParticipants;
	private List<String> tags;
}