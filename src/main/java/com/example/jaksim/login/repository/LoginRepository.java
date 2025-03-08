package com.example.jaksim.login.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jaksim.login.entity.Login;
import com.example.jaksim.login.entity.SocialType;

@Repository
public interface LoginRepository extends JpaRepository<Login, UUID> {
	Optional<Login> findByUserId(UUID userId);
	Optional<Login> findByTokenVersion(UUID tokenVersion);
	Optional<Login> findBySocialUserIdAndSocial(String socialUserId, SocialType socialType);
}