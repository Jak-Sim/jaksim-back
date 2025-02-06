package com.example.jaksim.challenge.service;

import com.example.jaksim.challenge.entity.Mission;
import com.example.jaksim.challenge.repository.MissionRepository;
import com.example.jaksim.challenge.entity.UserChallenge;
import com.example.jaksim.challenge.repository.UserChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PointService {

    private final UserChallengeRepository userChallengeRepository;
    private final MissionRepository missionRepository;

    @Autowired
    public PointService(UserChallengeRepository userChallengeRepository, MissionRepository missionRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.missionRepository = missionRepository;
    }

    // 포인트 적립
    @Transactional
    public void earnPoints(Long challengeId, String userUUID, Long missionId) {
        Mission mission = missionRepository.findByMissionId(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        UserChallenge userChallenge = userChallengeRepository.findByUserUserUuidAndChallengeChallengeId(UUID.fromString(userUUID), challengeId)
                .orElseThrow(() -> new RuntimeException("User not enrolled in challenge"));

        // 미션 완료 시 포인트 적립
        userChallenge.setPoints(userChallenge.getPoints() + mission.getCompletionPoints());
        userChallengeRepository.save(userChallenge);
    }

    // 포인트 사용
    @Transactional
    public void spendPoints(Long challengeId, String userUUID, int points) {
        UserChallenge userChallenge = userChallengeRepository.findByUserUserUuidAndChallengeChallengeId(UUID.fromString(userUUID), challengeId)
                .orElseThrow(() -> new RuntimeException("User not enrolled in challenge"));

        if (userChallenge.getPoints() < points) {
            throw new RuntimeException("Not enough points");
        }

        // 포인트 차감
        userChallenge.setPoints(userChallenge.getPoints() - points);
        userChallenge.setTotalPointsSpent(userChallenge.getTotalPointsSpent() + points);
        userChallengeRepository.save(userChallenge);
    }
}
