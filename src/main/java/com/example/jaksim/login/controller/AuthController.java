package com.example.jaksim.login.controller;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@GetMapping("/auth")
	public ResponseEntity<Void> authenticate(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String token = authHeader.substring(7);
		try {
			// JWT 검증
			Claims claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// @PostMapping("")
	// public ResponseEntity<ResponseEntity>
}