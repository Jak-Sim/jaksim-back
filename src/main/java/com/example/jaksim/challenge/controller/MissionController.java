package com.example.jaksim.challenge.controller;

import com.example.jaksim.challenge.dto.mission.MissionDto;
import com.example.jaksim.challenge.service.MissionService;
import com.example.jaksim.challenge.service.PointService;  // 포인트 서비스 추가
import com.example.jaksim.common.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/challenges/{challengeId}/missions")
public class MissionController {

    private final MissionService missionService;
    private final PointService pointService;  // 포인트 서비스 주입

    @Autowired
    public MissionController(MissionService missionService, PointService pointService) {
        this.missionService = missionService;
        this.pointService = pointService;
    }

    // 미션 목록 조회
    @GetMapping
    public ResponseEntity<ResponseDto> getMissions(
        @PathVariable UUID challengeId) {
        List<MissionDto> missionDtos = missionService.getMissions(challengeId);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "미션 목록 조회 성공", missionDtos), HttpStatus.OK);
    }

    // 미션 상세 조회
    @GetMapping("/{missionId}")
    public ResponseEntity<ResponseDto> getMission(
        @PathVariable UUID challengeId,
        @PathVariable UUID missionId) {
        MissionDto missionDto = missionService.getMission(challengeId, missionId);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "미션 상세 조회 성공", missionDto), HttpStatus.OK);
    }

    // 미션 생성
    @PostMapping
    public ResponseEntity<ResponseDto> createMission(
        @PathVariable UUID challengeId,
        @RequestBody MissionDto missionDto) {
        MissionDto createdMission = missionService.createMission(challengeId, missionDto);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "미션 생성 성공", createdMission), HttpStatus.CREATED);
    }

    // 미션 삭제
    @DeleteMapping("/{missionId}")
    public ResponseEntity<ResponseDto> deleteMission(
        @PathVariable UUID challengeId,
        @PathVariable UUID missionId) {
        missionService.deleteMission(challengeId, missionId);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "미션 삭제 성공"), HttpStatus.OK);
    }

    // 미션 완료시 포인트 적립
    @PostMapping("/{missionId}/complete")
    public ResponseEntity<ResponseDto> completeMission(
        @PathVariable UUID challengeId,
        @PathVariable UUID missionId,
        @RequestParam UUID userId) {

        // 포인트 적립
        pointService.earnPoints(challengeId, userId, missionId);
        
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "미션 완료 및 포인트 적립 완료"), HttpStatus.OK);
    }

    // 미션 포인트 사용
    @PostMapping("/{missionId}/spend")
    public ResponseEntity<ResponseDto> spendPoints(
        @PathVariable UUID challengeId,
        @PathVariable UUID missionId,
        @RequestParam UUID userId,
        @RequestParam int points) {
        // 포인트 사용
        pointService.spendPoints(challengeId, userId, points);
        
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "포인트 사용 완료"), HttpStatus.OK);
    }
}
