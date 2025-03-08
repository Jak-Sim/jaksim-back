package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RewardRepository extends JpaRepository<Reward, UUID> {
    // 챌린지 ID로 리워드를 조회
    List<Reward> findByChallengeId(UUID challengeId);

}
