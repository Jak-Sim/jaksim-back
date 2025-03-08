package com.example.jaksim.challenge.dto.reward;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.jaksim.challenge.entity.RewardRequest;

public class RewardRequestDto {

    private UUID requestId;
    private UUID rewardId;
    private boolean approved;
    private String rejectionReason;
    private LocalDateTime requestDate;

    public RewardRequestDto(UUID requestId, UUID rewardId, boolean approved, String rejectionReason, LocalDateTime requestDate) {
        this.requestId = requestId;
        this.rewardId = rewardId;
        this.approved = approved;
        this.rejectionReason = rejectionReason;
        this.requestDate = requestDate;
    }

    public RewardRequestDto(RewardRequest rewardRequest) {
        this.requestId = rewardRequest.getRequestId();
        this.rewardId = rewardRequest.getReward().getRewardId();
        this.rejectionReason = rewardRequest.getRejectionReason();
    }

    // Getter, Setter
    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getRewardId() {
        return rewardId;
    }

    public void setRewardId(UUID rewardId) {
        this.rewardId = rewardId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
