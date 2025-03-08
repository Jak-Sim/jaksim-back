package com.example.jaksim.user.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id", columnDefinition = "binary(16)", updatable = false, nullable = false)
	private UUID userId;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = true, unique = true)
	private String email;

	@Column
	private String social;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@ElementCollection
	@CollectionTable(name = "user_challenges", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "challenge_id")
	private List<UUID> challengeIds;

}