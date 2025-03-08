package com.example.jaksim.challenge.dto.challenge;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class ChallengeListResponse {

	@Builder.Default
	private List<ChallengeSummary> challenges = new ArrayList<>();

	public static ChallengeListResponse of(UUID challengeId, String name, String backgroundImage, UUID createdUserId, LocalDateTime createdAt) {
		ChallengeSummary summary = ChallengeSummary.of(challengeId, name, backgroundImage, false, createdUserId, createdAt);
		return ChallengeListResponse.builder()
				.challenges(List.of(summary))
				.build();
	}

	public static ChallengeListResponse of(List<ChallengeSummary> challenges) {
		return ChallengeListResponse.builder()
				.challenges(challenges)
				.build();
	}

	// 내부 클래스
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ChallengeSummary {
		private UUID challengeId;
		private String name;
		private String backgroundImage;
		private boolean isPublic;
		private UUID createdUserId;
		private LocalDateTime createdAt;
		private int remainingPoint;
		private int activeMissionsCount;
		private int participantsCount;

		public static ChallengeSummary of(UUID challengeId, String name, String backgroundImage,
										  boolean isPublic, UUID createdUserId, LocalDateTime createdAt) {
			return ChallengeSummary.builder()
					.challengeId(challengeId)
					.name(name)
					.backgroundImage(backgroundImage)
					.isPublic(isPublic)
					.createdUserId(createdUserId)
					.createdAt(createdAt)
					.build();
		}

		public static ChallengeSummary of(UUID challengeId, String name, String backgroundImage,
										  boolean isPublic, UUID createdUserId, LocalDateTime createdAt,
										  int remainingPoint, int activeMissionsCount, int participantsCount) {
			return ChallengeSummary.builder()
					.challengeId(challengeId)
					.name(name)
					.backgroundImage(backgroundImage)
					.isPublic(isPublic)
					.createdUserId(createdUserId)
					.createdAt(createdAt)
					.remainingPoint(remainingPoint)
					.activeMissionsCount(activeMissionsCount)
					.participantsCount(participantsCount)
					.build();
		}
	}
}