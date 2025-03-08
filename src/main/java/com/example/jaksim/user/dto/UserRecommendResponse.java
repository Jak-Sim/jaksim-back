package com.example.jaksim.user.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecommendResponse {
    private List<UserDto> relatedUsers;
    private int count;

    @Builder
    public static class UserDto {
        private Long userId;
        private UUID userUuid;
        private String username;
        private String email;
        private List<Long> challengeIds;
    }
}