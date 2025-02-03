package com.example.jaksim.challenge.service;

import com.example.jaksim.challenge.dto.reward.RewardDto;
import com.example.jaksim.challenge.dto.reward.RewardRequestDto;
import com.example.jaksim.challenge.entity.Reward;
import com.example.jaksim.challenge.entity.RewardRequest;
import com.example.jaksim.challenge.repository.RewardRepository;
import com.example.jaksim.challenge.repository.RewardRequestRepository;
import com.example.jaksim.challenge.repository.ChallengeRepository;
import com.example.jaksim.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.jaksim.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;
    private final RewardRequestRepository rewardRequestRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RewardService(RewardRepository rewardRepository, RewardRequestRepository rewardRequestRepository,
                         ChallengeRepository challengeRepository, UserRepository userRepository) {
        this.rewardRepository = rewardRepository;
        this.rewardRequestRepository = rewardRequestRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    // 챌린지에 속한 리워드 조회
    public List<RewardDto> getRewards(Long challengeId) {
        List<Reward> rewards = rewardRepository.findByChallengeChallengeId(challengeId);
        return rewards.stream()
                .map(reward -> {
                    RewardDto rewardDto = new RewardDto();
                    rewardDto.setRewardId(reward.getRewardId());
                    rewardDto.setName(reward.getRewardName());  // 필드명 수정
                    rewardDto.setDescription(reward.getRewardDetail());  // 필드명 수정
                    rewardDto.setRequiredPoints(reward.getRequiredPoints());
                    rewardDto.setRemainingCount(reward.getRemainingQuantity());  // 필드명 수정
                    rewardDto.setRewardImages(reward.getRewardImage());  // 필드명 수정
                    rewardDto.setExpirationDate(reward.getExpiryDate());  // 필드명 수정
                    rewardDto.setApprovalRequired(reward.isRequireApproval());  // 필드명 수정
                    return rewardDto;
                })
                .collect(Collectors.toList());
    }

    // 리워드 요청
    @Transactional
    public RewardRequestDto requestReward(Long userId, Long rewardId, int requestedPoints) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Reward not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (reward.getRemainingQuantity() <= 0) {
            throw new RuntimeException("The reward has no remaining quantity");
        }

        if (requestedPoints < reward.getRequiredPoints()) {
            throw new RuntimeException("Not enough points to claim this reward");
        }

        RewardRequest rewardRequest = new RewardRequest();
        rewardRequest.setUser(user);
        rewardRequest.setReward(reward);
        rewardRequest.setRequestedPoints(requestedPoints);
        rewardRequest.setRequestedAt(java.time.LocalDateTime.now());
        rewardRequest.setIsApproved(false);

        rewardRequestRepository.save(rewardRequest);

        return new RewardRequestDto(rewardRequest);
    }

    // 챌린지장이 리워드 요청 승인
    @Transactional
    public void approveRewardRequest(Long requestId) {
        RewardRequest rewardRequest = rewardRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        rewardRequest.setIsApproved(true);
        rewardRequest.setApprovedAt(java.time.LocalDateTime.now());

        Reward reward = rewardRequest.getReward();
        reward.setRemainingQuantity(reward.getRemainingQuantity() - 1);

        rewardRepository.save(reward);
        rewardRequestRepository.save(rewardRequest);
    }

    // 리워드 거부
    @Transactional
    public void rejectRewardRequest(Long requestId, String reason) {
        RewardRequest rewardRequest = rewardRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        rewardRequest.setRejectionReason(reason);
        rewardRequest.setIsApproved(false);

        rewardRequestRepository.save(rewardRequest);
    }
}
