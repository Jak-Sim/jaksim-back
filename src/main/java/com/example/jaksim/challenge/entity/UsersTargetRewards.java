package com.example.jaksim.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users_target_rewards")
public class UsersTargetRewards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_target_reward_id")
    private Integer userTargetRewardId;

    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public UsersTargetRewards() {
    }

    public UsersTargetRewards(UUID userId, Reward reward) {
        this.userId = userId;
        this.reward = reward;
    }
}