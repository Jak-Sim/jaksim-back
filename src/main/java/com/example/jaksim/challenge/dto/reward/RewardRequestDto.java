package com.example.jaksim.challenge.dto.reward;

import java.time.LocalDateTime;

import com.example.jaksim.challenge.entity.RewardRequest;

public class RewardRequestDto {

    private Long requestId;
    private Long rewardId;
    private Long userId;
    private boolean approved;
    private String rejectionReason;
    private LocalDateTime requestDate;

    public RewardRequestDto(Long requestId, Long rewardId, Long userId, boolean approved, String rejectionReason, LocalDateTime requestDate) {
        this.requestId = requestId;
        this.rewardId = rewardId;
        this.userId = userId;
        this.approved = approved;
        this.rejectionReason = rejectionReason;
        this.requestDate = requestDate;
    }

    public RewardRequestDto(RewardRequest rewardRequest) {
        this.requestId = rewardRequest.getRequestId();
        this.userId = rewardRequest.getUser().getUserId();
        this.rewardId = rewardRequest.getReward().getRewardId();
        this.rejectionReason = rewardRequest.getRejectionReason();
    }

    // Getter, Setter
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
