package com.example.jaksim.challenge.dto.reward;

import java.time.LocalDate;
import java.util.UUID;

public class RewardDto {

    private UUID rewardId;
    private String name;  // 필드명 변경
    private String description;  // 필드명 변경
    private int requiredPoints;
    private int remainingCount;
    private String rewardImages;  // 필드명 변경
    private boolean approvalRequired;  // 필드명 변경
    private LocalDate expirationDate;  // 필드명 변경

    // 기본 생성자
    public RewardDto() {}

    // 모든 필드를 포함하는 생성자
    public RewardDto(UUID rewardId, String name, String description, int requiredPoints,
                     int remainingCount, String rewardImages, boolean approvalRequired, LocalDate expirationDate) {
        this.rewardId = rewardId;
        this.name = name;
        this.description = description;
        this.requiredPoints = requiredPoints;
        this.remainingCount = remainingCount;
        this.rewardImages = rewardImages;
        this.approvalRequired = approvalRequired;
        this.expirationDate = expirationDate;
    }

    // Getter, Setter
    public UUID getRewardId() {
        return rewardId;
    }

    public void setRewardId(UUID rewardId) {
        this.rewardId = rewardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void setRemainingCount(int remainingCount) {
        this.remainingCount = remainingCount;
    }

    public String getRewardImages() {
        return rewardImages;
    }

    public void setRewardImages(String rewardImages) {
        this.rewardImages = rewardImages;
    }

    public boolean isApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
