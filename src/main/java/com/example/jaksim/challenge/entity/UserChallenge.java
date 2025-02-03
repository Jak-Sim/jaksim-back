package com.example.jaksim.challenge.entity;

import com.example.jaksim.user.entity.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_challenges")
public class UserChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(name = "points", nullable = false)
    private int points;  // 챌린지 별로 포인트 관리

    @Column(name = "total_points_earned", nullable = false)
    private int totalPointsEarned;  // 챌린지 내에서 유저가 얻은 총 포인트

    @Column(name = "total_points_spent", nullable = false)
    private int totalPointsSpent;  // 챌린지 내에서 유저가 사용한 포인트
}
