package com.example.jaksim.challenge.repository;

import com.example.jaksim.challenge.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}