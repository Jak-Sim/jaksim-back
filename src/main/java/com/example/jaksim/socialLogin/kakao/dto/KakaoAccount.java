package com.example.jaksim.socialLogin.kakao.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoAccount {
    private Profile profile;
    private String gender;
    private String email;
    private String age_range;

}
