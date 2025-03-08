package com.example.jaksim.common.security;

import com.example.jaksim.login.entity.Login;
import com.example.jaksim.login.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplement implements UserDetailsService {
	private final LoginRepository loginRepository;

	@Override
	public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {
		try {
			UUID userId = UUID.fromString(userIdentifier);
			Login login = loginRepository.findByUserId(userId)
					.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
			return new UserDetailsImplement(login, userIdentifier);
		} catch (IllegalArgumentException e) {
			throw new UsernameNotFoundException("유효하지 않은 UUID 형식입니다.");
		}
	}
}