package com.example.jaksim.socialLogin.kakao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

/**
 * @author        : mycom
 * @since         : 2023-05-22
 * FE에 유저정보를 주기위한 Dto생성
 */

@Getter
@ToString
public class KakaoResponseDto {

	private UUID memberId;

	private String memberName;

	private String profileImage;

	@Builder
	public KakaoResponseDto(UUID memberId, String memberName, String profileImage) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.profileImage = profileImage;
	}

}
