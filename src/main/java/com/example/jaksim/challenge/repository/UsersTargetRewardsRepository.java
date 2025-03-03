package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.UsersTargetRewards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface UsersTargetRewardsRepository extends JpaRepository<UsersTargetRewards, Long> {
    List<UsersTargetRewards> findByUserIdAndIsActiveTrue(UUID userId);
}
