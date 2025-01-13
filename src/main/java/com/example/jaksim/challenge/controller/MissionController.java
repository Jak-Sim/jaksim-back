package com.example.jaksim.challenge.controller;

import com.example.jaksim.challenge.dto.mission.MissionCreateRequest;
import com.example.jaksim.challenge.entity.Mission;
import com.example.jaksim.challenge.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping
    public ResponseEntity<Mission> createMission(
            @RequestBody MissionCreateRequest missionCreateRequest
            // @RequestParam("challengeId") Long challengeId,
            // @RequestParam("title") String title,
            // @RequestParam("content") String content,
            // @RequestParam("startDate") String startDate,
            // @RequestParam("endDate") String endDate,
            // @RequestParam("completionPoints") int completionPoints,
            // @RequestParam(value = "backgroundImages", required = false) List<MultipartFile> backgroundImages,
            // @RequestParam(value = "completionDeadline", required = false) String completionDeadline
    ) throws IOException {
        LocalDate endDate = missionCreateRequest.getEndDate();
        MissionCreateRequest request = new MissionCreateRequest();
        request.setChallengeId(missionCreateRequest.getChallengeId());
        request.setTitle(missionCreateRequest. getTitle());
        request.setContent(missionCreateRequest.getContent());
        request.setStartDate(missionCreateRequest.getStartDate());
        if (endDate.isEqual(null)) {
            request.setEndDate(null);
        }else{
            request.setEndDate(endDate);
        }
        request.setCompletionPoints(missionCreateRequest.getCompletionPoints());
        request.setBackgroundImages(missionCreateRequest.getBackgroundImages());
        request.setCompletionDeadline(missionCreateRequest.getCompletionDeadline());

        Mission createdMission = missionService.createMission(request);
        return ResponseEntity.ok(createdMission);
    }
}