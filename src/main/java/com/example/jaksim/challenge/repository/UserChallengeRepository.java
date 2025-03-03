package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.entity.UserChallenge;
import com.example.jaksim.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByUserUserUuidAndChallengeChallengeId(UUID userUuid, Long challengeId);
    Optional<UserChallenge> findByUserAndChallenge(User user, Challenge challenge);
}