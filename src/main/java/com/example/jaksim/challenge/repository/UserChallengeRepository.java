package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.entity.UserChallenge;
import com.example.jaksim.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, UUID> {
    Optional<UserChallenge> findByUserUserIdAndChallengeChallengeId(UUID userId, UUID challengeId);
    Optional<UserChallenge> findByUserAndChallenge(User user, Challenge challenge);
    List<UserChallenge> findByUser(User user);
    List<UserChallenge> findByChallengeChallengeIdInAndUserUserIdNot(Collection<UUID> challenge_challengeId, UUID user_userId);
}