package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.RewardRequest;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRequestRepository extends JpaRepository<RewardRequest, Long> {
    // 리워드 요청 ID로 조회
    Optional<RewardRequest> findByRequestId(UUID requestId);
}
