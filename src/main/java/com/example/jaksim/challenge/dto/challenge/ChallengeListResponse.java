package com.example.jaksim.challenge.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeListResponse {

	private List<ChallengeSummary> challenges;

	public ChallengeListResponse(List<ChallengeSummary> challenges) {
		this.challenges = challenges;
	}

	public ChallengeListResponse(Long challengeId, String name, String backgroundImage, String creatorUuid, LocalDateTime createdAt) {
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
		

		// 진행중인 미션 수

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
	}
}
