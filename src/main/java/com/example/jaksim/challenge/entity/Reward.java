package com.example.jaksim.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reward_id", updatable = false, nullable = false)
    private UUID rewardId;

    @Column(name = "challenge_id", columnDefinition = "binary(16)", nullable = false)
    private UUID challengeId;

    @Column(nullable = false)
    private String rewardName;

    @Column(nullable = false)
    private String rewardDetail;

    @Column(nullable = false)
    private int requiredPoints;

    @Column(nullable = false)
    private int remainingQuantity;

    @Column(nullable = false)
    private String rewardImage;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private boolean requireApproval;  // 챌린지장이 수락해야 하는지 여부

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
