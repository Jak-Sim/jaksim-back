package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    // 챌린지 ID로 리워드를 조회
    List<Reward> findByChallengeChallengeId(Long challengeId);
    List<Reward> findByChallenge(Challenge challenge);

}
