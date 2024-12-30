package com.example.jaksim.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.jaksim.common.jwt.JwtAuthFilter;
import com.example.jaksim.common.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
// @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // JwtAuthFilter 주입

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**") // H2 콘솔 예외 처리
            .disable() // CSRF 보호 비활성화
        );

        // CORS 설정
        http.cors(cors -> cors
            .configurationSource(corsConfigurationSource()) // CORS 설정 추가
        );

        // 세션을 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless로 설정
        );

        // 요청에 대한 인증 및 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/h2-console/**", "/nginx/test").permitAll() // Swagger, H2 콘솔, 테스트 경로 예외 처리
            .requestMatchers("/sign-in/kakao").permitAll() // 카카오 로그인 예외 처리
            .requestMatchers("/sign-up/nick-check").permitAll() // 닉네임 중복 체크 예외 처리
            .requestMatchers("/sign-up").permitAll()
            .requestMatchers("/challenge").permitAll() // 챌린지 관련 예외 처리
            .requestMatchers(request -> request.getRemoteAddr().equals("127.0.0.1")).permitAll() // 특정 IP에서의 접근 허용
            .anyRequest().authenticated() // 그 외의 요청들은 인증 필요
        );

        // JWT 필터 추가
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가

        // H2 콘솔을 iframe에서 사용할 수 있도록 설정
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("http://localhost");
        config.addAllowedOriginPattern("http://localhost:3000");
        
        config.addAllowedOrigin("http://localhost:8080"); // 포트 8080에서의 요청 허용
        config.addAllowedOrigin("http://113.30.28.42"); // 특정 IP에서의 요청 허용
        config.addAllowedOrigin("http://113.30.28.42:3000");
        // JWT 헤더를 CORS에서 노출하도록 설정
        config.addExposedHeader(JwtUtil.ACCESS_KEY);
        config.addExposedHeader(JwtUtil.REFRESH_KEY);
        
        // CORS 메서드 및 헤더 설정
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.validateAllowCredentials();

        // CORS 설정을 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }
}
