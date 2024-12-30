package com.example.jaksim.socialLogin.kakao.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.jaksim.common.ResponseDto;
import com.example.jaksim.common.jwt.JwtUtil;
import com.example.jaksim.common.jwt.RedisDao;
import com.example.jaksim.common.jwt.TokenDto;
import com.example.jaksim.login.dto.LoginRequest;
import com.example.jaksim.login.entity.Login;
import com.example.jaksim.login.entity.SocialType;
import com.example.jaksim.login.repository.LoginRepository;
import com.example.jaksim.socialLogin.kakao.client.KakaoClient;
import com.example.jaksim.socialLogin.kakao.dto.KakaoAccount;
import com.example.jaksim.socialLogin.kakao.dto.KakaoResponseDto;
import com.example.jaksim.socialLogin.kakao.dto.KakaoToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient client;
    private final LoginRepository loginRepository;
    private final JwtUtil jwtUtil;
    private final RedisDao redisDao;


    /**
     * 유저 정보를 얻기위한 url
     */
    @Value("${kakao.userApiUrl}")
    private String kakaoUserApiUrl;

    /**
     * 로그아웃을 하기위한 url 구현중
     */
    @Value("${kakao.logoutUrl}")
    private String kakaKologoutUrl;



    public ResponseEntity<ResponseDto> getKakaoInfo(final String request, final HttpServletResponse response) {

        //code를 이용해서 Kakao Api용 Accesstoekn과 Refresh토큰을 받아옴

        //request 에 있는 accessToekn 만 빼오면 됨  그럴려면 parse 해서 token 에 저장
        String accessToken;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(request);
            accessToken = rootNode.get("access_token").asText();
            //해당 토큰을 이용해서 유저정보를 요청하는 API요청을 보낸다
            ObjectMapper objectMapper2 = new ObjectMapper();
            System.out.println(accessToken);
            JsonNode rootnode2 = objectMapper2.readTree(String.valueOf(
                client.getKakaoInfo(new URI(kakaoUserApiUrl), "Bearer " + accessToken)));
            String kakaoAccount = rootnode2.get("id").asText();
            // 가져온 유저정보로 Email과 소셜로그인 정보로 이미 회원가입이 된 유저인지 확인
            Optional<Login> member = loginRepository.findByMemberUniqueIdAndSocial(kakaoAccount, SocialType.KAKAO);
            //로그인했는데 첫 로그인일시 회원가입
            if (member.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("AT", null);
                responseData.put("RT", null);
                responseData.put("nickname", null);
                responseData.put("social", "KAKAO");
                responseData.put("userUniqueId", kakaoAccount);

                return new ResponseEntity<>(new ResponseDto(208,"가입되지 않은 유저입니다", responseData), HttpStatus.ALREADY_REPORTED);
            }


            TokenDto tokenDto = jwtUtil.createAllToken(String.valueOf(member.get().getUserUuid()),String.valueOf(member.get().getTokenVersion()));
            response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
            response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("AT",tokenDto.getAccessToken() );
            responseData.put("RT",  tokenDto.getRefreshToken());
            responseData.put("nickname", "멤버 진행중");
            responseData.put("social", "KAKAO");

            return new ResponseEntity<>(new ResponseDto(209, "로그인에 성공하셨습니다.", responseData), HttpStatus.OK);
        } catch (URISyntaxException e) {
            log.error("Invalid URI syntax", e);
            return new ResponseEntity<>(new ResponseDto(401, "KAKAO API 에러"), HttpStatus.BAD_REQUEST);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



}
