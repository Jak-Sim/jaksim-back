package com.example.jaksim.challenge.dto.challenge;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChallengeListResponse {

	private List<ChallengeSummary> challenges;

	public ChallengeListResponse() {
		this.challenges = new ArrayList<>();
	}

	// 챌린지 요약 리스트를 받는 생성자
	public ChallengeListResponse(List<ChallengeSummary> challenges) {
		this.challenges = challenges;
	}

	// 단일 챌린지 정보를 받아 리스트로 리턴하는 생성자
	public ChallengeListResponse(Long challengeId, String name, String backgroundImage, String creatorUuid, LocalDateTime createdAt) {
		ChallengeSummary summary = new ChallengeSummary();
		summary.setChallengeId(challengeId);
		summary.setName(name);
		summary.setBackgroundImage(backgroundImage);
		summary.setCreatorUuid(creatorUuid);
		summary.setCreatedAt(createdAt);

		this.challenges = new ArrayList<>();
		this.challenges.add(summary);
	}

	// Getters and Setters
	public List<ChallengeSummary> getChallenges() {
		return challenges;
	}

	public void setChallenges(List<ChallengeSummary> challenges) {
		this.challenges = challenges;
	}

	// 내부 클래스: ChallengeSummary
	public static class ChallengeSummary {
		private Long challengeId;
		private String name;
		private String backgroundImage;
		private boolean isPublic;
		private String creatorUuid;
		private LocalDateTime createdAt;

		// 기본 생성자 추가
		public ChallengeSummary() {
		}

		// 전체 필드를 초기화하는 생성자 추가
		public ChallengeSummary(Long challengeId, String name, String backgroundImage,
								boolean isPublic, String creatorUuid, LocalDateTime createdAt) {
			this.challengeId = challengeId;
			this.name = name;
			this.backgroundImage = backgroundImage;
			this.isPublic = isPublic;
			this.creatorUuid = creatorUuid;
			this.createdAt = createdAt;
		}

		// Getters and Setters
		public Long getChallengeId() {
			return challengeId;
		}

		public void setChallengeId(Long challengeId) {
			this.challengeId = challengeId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBackgroundImage() {
			return backgroundImage;
		}

		public void setBackgroundImage(String backgroundImage) {
			this.backgroundImage = backgroundImage;
		}

		public boolean isPublic() {
			return isPublic;
		}

		public void setPublic(boolean isPublic) {
			this.isPublic = isPublic;
		}

		public String getCreatorUuid() {
			return creatorUuid;
		}

		public void setCreatorUuid(String creatorUuid) {
			this.creatorUuid = creatorUuid;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	}
}