package com.example.jaksim.challenge.dto.mission;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class MissionDto {
    private UUID missionId;
    private String title;
    private String content;
    private int completionPoints;
    private LocalDate startDate;
    private LocalDate endDate;
    private String backgroundImages;
    private String completionDeadline;
}
