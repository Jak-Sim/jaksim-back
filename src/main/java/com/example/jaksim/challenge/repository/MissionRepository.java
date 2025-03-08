package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MissionRepository extends JpaRepository<Mission, UUID> {
    Optional<Mission> findByMissionId(UUID missionId);
    List<Mission> findAllByChallengeChallengeId(UUID challengeId);
    Optional<Mission> findByMissionIdAndChallengeChallengeId(UUID missionId, UUID challengeId);
    int countByChallengeAndIsActive(Challenge challenge, int isActive);
    List<Mission> findByChallengeAndIsActiveOrderByCompletionPointsAsc(Challenge challenge, int isActive);
}