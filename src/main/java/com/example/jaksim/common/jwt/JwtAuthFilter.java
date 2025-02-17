package com.example.jaksim.common.jwt;

// import com.example.jaksim.common.jwt.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static com.example.jaksim.common.jwt.JwtUtil.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.jaksim.login.repository.LoginRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final LoginRepository loginRepository;
	private final RedisDao redisDao;

	@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		System.out.println("requestURI is " + requestURI);
		if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/swagger-ui.html")) {
			filterChain.doFilter(request, response);
			System.out.println("comming here? ");
			return; // 인증 건너뛰기
		}
	
		
		String access_token = jwtUtil.resolveToken(request, ACCESS_KEY);
		String refresh_token = jwtUtil.resolveToken(request, REFRESH_KEY);

		if (access_token != null) {
			if (jwtUtil.validateToken(access_token)) {
				setAuthentication(jwtUtil.getUserUuidFromToken(access_token));
			} else {
				jwtExceptionHandler(response, "403 : Invalid Token", HttpStatus.FORBIDDEN.value());
				return;
			}
		}
		
		filterChain.doFilter(request, response);
	}

	private void jwtExceptionHandler(jakarta.servlet.http.HttpServletResponse response, String msg, int statusCode) {
		
		response.setStatus(statusCode);

		response.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new TokenMessageDto(msg, statusCode));
			response.getWriter().write(json);
		} catch (Exception e){
			log.error(e.getMessage());
		}
	}

	public void setAuthentication(String userUuid) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = jwtUtil.createAuthentication(userUuid);
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}



}
