package com.example.jaksim.challenge.controller;

import com.example.jaksim.challenge.dto.reward.RewardDto;
import com.example.jaksim.challenge.dto.reward.RewardRequestDto;
import com.example.jaksim.challenge.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<RewardDto>> getRewards(@PathVariable Long challengeId) {
        List<RewardDto> rewards = rewardService.getRewards(challengeId);
        return ResponseEntity.ok(rewards);
    }

    // 리워드 요청
    @PostMapping("/request")
    public ResponseEntity<RewardRequestDto> requestReward(@PathVariable Long challengeId,
                                                          @RequestParam Long userId,
                                                          @RequestParam int requestedPoints) {
        RewardRequestDto rewardRequestDto = rewardService.requestReward(userId, challengeId, requestedPoints);
        return ResponseEntity.ok(rewardRequestDto);
    }

    // 리워드 승인
    @PostMapping("/approve/{requestId}")
    public ResponseEntity<Void> approveReward(@PathVariable Long requestId) {
        rewardService.approveRewardRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // 리워드 거부
    @PostMapping("/reject/{requestId}")
    public ResponseEntity<Void> rejectReward(@PathVariable Long requestId,
                                             @RequestParam String reason) {
        rewardService.rejectRewardRequest(requestId, reason);
        return ResponseEntity.ok().build();
    }
}
