package com.example.jaksim.socialLogin.kakao.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * 유저 프로필 정보 : 닉네임, 썸네일 img, 프로필 img
 * 카카오는 nickname과 프로필 이미지는 profile_필드명 으로 응답을 줘서 해당 Dto를 생성하였다.
 */
@Getter
@ToString
public class Profile {

    private String nickname;
    /**
     * kakao api자체에서 snake방식으로 전달하여 필드명 설정
     */
    private String thumbnail_image_url;
    /**
     * kakao api자체에서 snake방식으로 전달하여 필드명 설정
     */
    private String profile_image_url;
}
