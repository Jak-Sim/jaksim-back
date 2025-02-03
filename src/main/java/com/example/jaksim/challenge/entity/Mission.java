package com.example.jaksim.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "completion_points", nullable = false)
    private int completionPoints;

    @ElementCollection
    @CollectionTable(name = "mission_images", joinColumns = @JoinColumn(name = "mission_id"))
    @Column(name = "image_url")
    private String backgroundImages;

    @Column(name = "completion_deadline")
    private String completionDeadline;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}