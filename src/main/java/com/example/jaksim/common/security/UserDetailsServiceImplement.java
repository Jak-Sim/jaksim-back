package com.example.jaksim.common.security;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.jaksim.login.entity.Login;
import com.example.jaksim.login.repository.LoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplement implements UserDetailsService {
	private final LoginRepository loginRepository;

	@Override
	public UserDetails loadUserByUsername(String userUuid) throws UsernameNotFoundException {

		Login login = (Login)loginRepository.findByUserUuid(UUID.fromString(userUuid)).orElseThrow(
			()-> new UsernameNotFoundException("존재하지 않는 유저 네임입니다.")
		);
		return new UserDetailsImplement(login, String.valueOf(login.getUserUuid()));
	}


}
