package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByMissionId(Long missionId);
    List<Mission> findAllByChallengeChallengeId(Long challengeId);
    Optional<Mission> findByMissionIdAndChallengeChallengeId(Long missionId, Long challengeId);
    int countByChallengeAndIsActive(Challenge challenge, int isActive);
    List<Mission> findByChallengeAndIsActiveOrderByCompletionPointsAsc(Challenge challenge, int isActive);
}