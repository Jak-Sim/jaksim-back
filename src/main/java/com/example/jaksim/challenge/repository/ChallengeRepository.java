package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
	Challenge findByChallengeId(UUID challengeId);
	Challenge findByParticipationCode(String participationCode);
	Page<Challenge> findByChallengeIdIn(List<UUID> challengeIds, Pageable pageable);
	Page<Challenge> findByIsPublicTrue(Pageable pageable);
}
