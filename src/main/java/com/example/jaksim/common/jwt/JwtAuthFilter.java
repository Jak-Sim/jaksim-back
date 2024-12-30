package com.example.jaksim.common.jwt;

import static com.example.jaksim.common.jwt.JwtUtil.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.springframework.http.HttpStatus;
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

		if(access_token != null) {
			if(jwtUtil.validateToken(access_token)) {
				if (redisDao.getValues(request.getHeader("Access_key").substring(7))!= null) {
					jwtExceptionHandler(response, "410 : This token already Logged Out ", 410);
					return;
				}
				setAuthentication(jwtUtil.getMemberInfoFromToken(access_token));
			} else {
				jwtExceptionHandler(response, "403 : Wrong token", HttpStatus.FORBIDDEN.value());
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

	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = jwtUtil.createAuthentication(username);
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}



}
