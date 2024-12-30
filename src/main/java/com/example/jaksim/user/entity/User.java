package com.example.jaksim.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(updatable = false, unique = true, nullable = false)
	private UUID userUuid;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = true, unique = true)
	private String email;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@ElementCollection
    @CollectionTable(name = "user_challenges", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "challenge_id")
    private List<Long> challengeIds;
	


	public User() {
		this.userUuid = UUID.randomUUID();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UUID getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(UUID userUuid) {
		this.userUuid = userUuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public List<Long> getChallengeIds(){
		return challengeIds;
	}

	public void setChallengeIds(List<Long> challengeIds){
		this.challengeIds = challengeIds;
	}
}
