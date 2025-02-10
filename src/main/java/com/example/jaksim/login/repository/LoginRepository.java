package com.example.jaksim.login.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jaksim.login.entity.Login;
import com.example.jaksim.login.entity.SocialType;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

	Optional<Login> findByMemberUniqueIdAndSocial(String memberUniqueId, SocialType socialType);

	Optional<Object> findByMemberUniqueId(String memberUniqueId);

	Optional<Object> findByUserUuid(UUID memberUniqueId);

	Optional<Login> findByTokenVersion(UUID tokenVersion);
}