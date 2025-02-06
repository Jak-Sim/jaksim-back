package com.example.jaksim.challenge.entity;

import com.example.jaksim.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reward_requests")
public class RewardRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;

    @Column(nullable = false)
    private int requestedPoints;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Column(nullable = false)
    private boolean isApproved;  // 챌린지장이 수락했는지 여부

    @Column(nullable = true)
    private LocalDateTime approvedAt;  // 수락 일시

    @Column(nullable = true)
    private String rejectionReason;  // 거부 이유

    public void setIsApproved(boolean b) {

        throw new UnsupportedOperationException("Unimplemented method 'setIsApproved'");
    }
}
