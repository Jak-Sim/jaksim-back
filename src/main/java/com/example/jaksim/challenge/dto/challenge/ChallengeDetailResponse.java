package com.example.jaksim.challenge.dto.challenge;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeDetailResponse {

	private Long challengeId;
	private String name;
	private String backgroundImage;
	private boolean isPublic;
	private int currentParticipants;
	private int maxParticipants;
	private List<String> tags;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String creatorUserUuid;
	private List<ParticipantResponse> participants; 


	public ChallengeDetailResponse(Long challengeId, String name, String backgroundImage, boolean isPublic, int currentParticipants, int maxParticipants, List<String> tags, LocalDateTime createdAt,LocalDateTime updatedAt, String creatorUserUuid) {
        this.challengeId = challengeId;
        this.name = name;
        this.backgroundImage = backgroundImage;
        this.isPublic = isPublic;
        this.currentParticipants = currentParticipants;
        this.maxParticipants = maxParticipants;
        this.tags = tags;
        this.createdAt = createdAt;
		this.updatedAt = updatedAt;
        this.creatorUserUuid = creatorUserUuid;
    }

	public ChallengeDetailResponse(Long challengeId, String name, String backgroundImage, boolean isPublic, int currentParticipants, int maxParticipants, List<String> tags, LocalDateTime createdAt,LocalDateTime updatedAt, String creatorUserUuid, List<ParticipantResponse> participants) {
        this.challengeId = challengeId;
        this.name = name;
        this.backgroundImage = backgroundImage;
        this.isPublic = isPublic;
        this.currentParticipants = currentParticipants;
        this.maxParticipants = maxParticipants;
        this.tags = tags;
        this.createdAt = createdAt;
		this.updatedAt = updatedAt;
        this.creatorUserUuid = creatorUserUuid;
        this.participants = participants;
    }


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

	public int getCurrentParticipants() {
		return currentParticipants;
	}

	public void setCurrentParticipants(int currentParticipants) {
		this.currentParticipants = currentParticipants;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatorUserUuid() {
		return creatorUserUuid;
	}

	public void setCreatorUserUuid(String creatorUserUuid) {
		this.creatorUserUuid = creatorUserUuid;
	}

	public List<ParticipantResponse> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantResponse> participants) {
        this.participants = participants;
    }
}
