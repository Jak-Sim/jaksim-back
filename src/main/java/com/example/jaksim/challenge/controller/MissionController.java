package com.example.jaksim.challenge.controller;

import com.example.jaksim.challenge.dto.mission.MissionCreateRequest;
import com.example.jaksim.challenge.entity.Mission;
import com.example.jaksim.challenge.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping
    public ResponseEntity<Mission> createMission(
            @RequestParam("challengeId") Long challengeId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("completionPoints") int completionPoints,
            @RequestParam(value = "backgroundImages", required = false) List<MultipartFile> backgroundImages,
            @RequestParam(value = "completionDeadline", required = false) String completionDeadline
    ) throws IOException {
        MissionCreateRequest request = new MissionCreateRequest();
        request.setChallengeId(challengeId);
        request.setTitle(title);
        request.setContent(content);
        request.setStartDate(LocalDate.parse(startDate));
        request.setEndDate(endDate.isEmpty() ? null : LocalDate.parse(endDate));
        request.setCompletionPoints(completionPoints);
        request.setBackgroundImages(backgroundImages);
        request.setCompletionDeadline(completionDeadline);

        Mission createdMission = missionService.createMission(request);
        return ResponseEntity.ok(createdMission);
    }
}