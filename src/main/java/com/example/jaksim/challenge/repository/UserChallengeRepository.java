package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByUserUserUuidAndChallengeChallengeId(String userUuid, Long challengeId);
}