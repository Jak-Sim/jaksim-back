package com.example.jaksim.challenge.controller;

import com.example.jaksim.challenge.dto.reward.RewardDto;
import com.example.jaksim.challenge.dto.reward.RewardRequestDto;
import com.example.jaksim.challenge.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/challenges/{challengeId}/rewards")
public class RewardController {

    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    // 리워드 조회
    @GetMapping
    public ResponseEntity<List<RewardDto>> getRewards(@PathVariable UUID challengeId) {
        List<RewardDto> rewards = rewardService.getRewards(challengeId);
        return ResponseEntity.ok(rewards);
    }

    // 리워드 요청
    @PostMapping("/request")
    public ResponseEntity<RewardRequestDto> requestReward(@PathVariable UUID challengeId,
                                                          @RequestParam UUID userId,
                                                          @RequestParam int requestedPoints) {
        RewardRequestDto rewardRequestDto = rewardService.requestReward(userId, challengeId, requestedPoints);
        return ResponseEntity.ok(rewardRequestDto);
    }

    // 리워드 승인
    @PostMapping("/approve/{rewardRequestId}")
    public ResponseEntity<Void> approveReward(@PathVariable UUID rewardRequestId) {
        rewardService.approveRewardRequest(rewardRequestId);
        return ResponseEntity.ok().build();
    }

    // 리워드 거부
    @PostMapping("/reject/{rewardRequestId}")
    public ResponseEntity<Void> rejectReward(@PathVariable UUID rewardRequestId,
                                             @RequestParam String reason) {
        rewardService.rejectRewardRequest(rewardRequestId, reason);
        return ResponseEntity.ok().build();
    }
}
