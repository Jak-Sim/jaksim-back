package com.example.jaksim.user.repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jaksim.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByUsername(String username);
	Optional<User> findByUserId(UUID userId);
	@Query("SELECT u FROM User u JOIN u.challengeIds c WHERE c = :challengeId")
    List<User> findUsersByChallengeId(@Param("challengeId") UUID challengeId);
}
