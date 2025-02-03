package com.example.jaksim.challenge.dto.mission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissionDto {
    private Long missionId;
    private String title;
    private String content;
    private int completionPoints;
}