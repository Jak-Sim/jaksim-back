package com.example.jaksim.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jaksim.user.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Username 중복 여부 확인
	public boolean isUsernameTaken(String username) {
		return userRepository.findByUsername(username).isPresent();
	}
}
