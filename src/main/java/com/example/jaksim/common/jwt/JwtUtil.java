package com.example.jaksim.common.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.jaksim.common.security.RefreshTokenRepository;
import com.example.jaksim.common.security.UserDetailsServiceImplement;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
	@Value("${jwt.secret.key}")
	private String secretKey;

	public static final String ACCESS_KEY = "ACCESS_KEY";
	public static final String REFRESH_KEY = "REFRESH_KEY";

	// public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";


	private static final long ACCESS_TIME =  24 * 60 * 60 * 1000L;

	private static final long REFRESH_TIME = 14 * 24 * 60 * 60 * 1000L;


	private final UserDetailsServiceImplement userDetailsServiceImplement;
	private final RefreshTokenRepository refreshTokenRepository;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private Key key;
	private RedisDao redisDao;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createAccessToken(String userUuid, String type) {
		Date date = new Date();
		long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

		return BEARER_PREFIX
			+ Jwts.builder()
			.setSubject(userUuid)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.setIssuedAt(date)
			.setExpiration(new Date(date.getTime() + time))
			.compact();
	}
	public String createRefreshToken(String tokenVersion, String type) {
		Date date = new Date();
		long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

		return BEARER_PREFIX
			+ Jwts.builder()
			.setSubject(tokenVersion)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.setIssuedAt(date)
			.setExpiration(new Date(date.getTime() + time))
			.compact();
	}

	public String extractTokenVersion(String refreshToken){
		try{
			Claims claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(refreshToken)
				.getBody();
			return claims.getSubject();
		}catch(SignatureException e){
			throw  new IllegalArgumentException();
		}
	}

	public TokenDto createAllToken(String memberUniqueId, String tokenVersion) {
		return new TokenDto(createAccessToken(memberUniqueId, "Access"), createRefreshToken(tokenVersion, "Refresh"));
	}

	public String resolveToken(HttpServletRequest httpServletRequest, String token) {
		String tokenName = token.equals("ACCESS_KEY") ? ACCESS_KEY : REFRESH_KEY;
		String bearerToken = httpServletRequest.getHeader(tokenName);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("유효하지 않은 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT token 입니다.");
			return false;
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.info("잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	public String getMemberInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public String getUserUuidFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject(); 
	}
	
	public Authentication createAuthentication(String userUUID) {
		UserDetails userDetails = userDetailsServiceImplement.loadUserByUsername(userUUID);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	public Boolean refreshTokenValidation(String token) {
		if (!validateToken(token)) return false;

		redisDao.getValues(getMemberInfoFromToken(token)).equals(token);
		// Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmailId(getMemberInfoFromToken(token));
		// return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
		// return redisDao.getValues(getMemberInfoFromToken(token)).equals(token);
		return true;
	}

	public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader(ACCESS_KEY, accessToken);
	}
}
