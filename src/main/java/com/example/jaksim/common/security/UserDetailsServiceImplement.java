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
			UUID uuid = UUID.fromString(userIdentifier);
			Login login = (Login) loginRepository.findByUserUuid(uuid).orElseThrow(
					() -> new UsernameNotFoundException("존재하지 않는 유저 네임입니다.")
			);
			return new UserDetailsImplement(login, String.valueOf(login.getUserUuid()));
		} catch (IllegalArgumentException e) {
			Login login = (Login) loginRepository.findByMemberUniqueId(userIdentifier).orElseThrow(
					() -> new UsernameNotFoundException("존재하지 않는 유저 네임입니다.")
			);
			return new UserDetailsImplement(login, String.valueOf(login.getUserUuid()));
		}
	}
}