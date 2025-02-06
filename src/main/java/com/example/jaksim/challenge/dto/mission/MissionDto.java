package com.example.jaksim.challenge.dto.mission;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MissionDto {
    private Long missionId;
    private String title;
    private String content;
    private int completionPoints;
    private LocalDate startDate;
    private LocalDate endDate;
    private String backgroundImages;
    private String completionDeadline;
}
