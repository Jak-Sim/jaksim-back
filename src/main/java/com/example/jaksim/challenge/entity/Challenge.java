package com.example.jaksim.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Table(name = "challenges")
public class Challenge {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "challenge_id", updatable = false, nullable = false)
	private UUID challengeId;

	@Column(nullable = false)
	private String name;

	@Column(name = "background_image")
	private String backgroundImage;

	@Column(name = "is_public", nullable = false)
	private boolean isPublic;

	@Column(name = "max_participants", nullable = false)
	private int maxParticipants;

	@Column(name = "current_participants", nullable = false)
	private int currentParticipants;

	@ElementCollection
	@CollectionTable(name = "challenge_tags", joinColumns = @JoinColumn(name = "challenge_id"))
	@Column(name = "tag")
	private List<String> tags;

	@Column(name = "created_user_id", nullable = false)
	private UUID createdUserId;

	@Column(name = "participation_code", unique = true)
	private String participationCode;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

}
