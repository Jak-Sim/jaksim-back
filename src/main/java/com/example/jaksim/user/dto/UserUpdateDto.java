package com.example.jaksim.user.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class UserUpdateDto {
    private UUID userUuid;
    private String username;
    private String email;
}