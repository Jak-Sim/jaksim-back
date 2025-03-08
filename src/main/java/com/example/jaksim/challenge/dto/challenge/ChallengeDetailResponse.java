package com.example.jaksim.challenge.dto.challenge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeDetailResponse {

	private UUID challengeId;
	private String name;
	private String backgroundImage;
	private boolean isPublic;
	private int currentParticipants;
	private int maxParticipants;
	private List<String> tags;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private UUID createdUserId;
	private List<ParticipantResponse> participants;

	public static ChallengeDetailResponse of(
			UUID challengeId,
			String name,
			String backgroundImage,
			boolean isPublic,
			int currentParticipants,
			int maxParticipants,
			List<String> tags,
			LocalDateTime createdAt,
			LocalDateTime updatedAt,
			UUID createdUserId) {

		return ChallengeDetailResponse.builder()
				.challengeId(challengeId)
				.name(name)
				.backgroundImage(backgroundImage)
				.isPublic(isPublic)
				.currentParticipants(currentParticipants)
				.maxParticipants(maxParticipants)
				.tags(tags)
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.createdUserId(createdUserId)
				.build();
	}

	public static ChallengeDetailResponse of(
			UUID challengeId,
			String name,
			String backgroundImage,
			boolean isPublic,
			int currentParticipants,
			int maxParticipants,
			List<String> tags,
			LocalDateTime createdAt,
			LocalDateTime updatedAt,
			UUID createdUserId,
			List<ParticipantResponse> participants) {

		return ChallengeDetailResponse.builder()
				.challengeId(challengeId)
				.name(name)
				.backgroundImage(backgroundImage)
				.isPublic(isPublic)
				.currentParticipants(currentParticipants)
				.maxParticipants(maxParticipants)
				.tags(tags)
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.createdUserId(createdUserId)
				.participants(participants)
				.build();
	}
}