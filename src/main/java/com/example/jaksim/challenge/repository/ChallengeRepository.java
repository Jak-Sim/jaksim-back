package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
@EnableJpaRepositories
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

	List<Challenge> findAllByOrderByCreatedAtDesc();

	Challenge findByChallengeId(Long challengeId);

	List<Challenge> findByCreatorUuid(String creatorUserUuid);

	Challenge findByParticipationCode(String participationCode);

	Page<Challenge> findByChallengeIdIn(List<Long> challengeIds, Pageable pageable);
}
