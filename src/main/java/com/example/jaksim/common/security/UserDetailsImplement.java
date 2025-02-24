package com.example.jaksim.common.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.jaksim.login.entity.Login;
import com.example.jaksim.login.entity.Login;

public class UserDetailsImplement implements UserDetails {

	private final Login login;
	private final String userUuid;
	public UserDetailsImplement(Login login, String userUuid) {
		this.login = login;
		this.userUuid = userUuid;
	}
	public Login getMember(){
		return login;
	}
	//
	// @Override
	// public Collection<? extends GrantedAuthority> getAuthorities() {
	// 	String authority = login.getAuthority();
	// 	SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
	// 	Collection<GrantedAuthority> authorities = new ArrayList<>();
	// 	authorities.add(simpleGrantedAuthority);
	// 	return authorities;
	// }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.userUuid;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
