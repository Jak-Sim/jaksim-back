package com.example.jaksim.challenge.controller;

import com.example.jaksim.challenge.dto.challenge.*;
import com.example.jaksim.challenge.service.ChallengeService;
import com.example.jaksim.common.ResponseDto;

import com.example.jaksim.common.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/challenge")
@Tag(name = "Challenge API", description = "챌린지 관련 기능 제공")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }
    // 내가 가입한 챌린지 목록 조회 + 목표 보상까지 남은 포인트

    @Operation(summary = "챌린지 목록 조회", description = "페이지를 기준으로 챌린지 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseDto> getChallenges(
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page) {
        ChallengeListResponse challenges = challengeService.getChallenges(page);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 목록 조회 성공", challenges), HttpStatus.OK);
    }

    @Operation(
            summary = "내 챌린지 목록 조회",
            description = "현재 로그인한 사용자의 챌린지 목록을 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/mine")
    public ResponseEntity<ResponseDto> getPersonalChallenges(
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @AuthUser UUID userId) {
        Map<String, Object> responseData = challengeService.getPersonalChallenges(page, userId);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 목록 조회 성공", responseData), HttpStatus.OK);
    }

    @Operation(summary = "챌린지 상세 조회", description = "참여 코드나 챌린지 ID로 챌린지 상세 데이터를 조회합니다.")
    @GetMapping("/detail")
    public ResponseEntity<ResponseDto> getChallengeDetail(
            @RequestParam(required = false) UUID challengeId,
            @RequestParam(required = false) String participationCode) {
        ChallengeDetailResponse challengeDetail;
        if (challengeId != null) {
            challengeDetail = challengeService.getChallengeDetail(challengeId);
        } else if (participationCode != null && !participationCode.isEmpty()) {
            challengeDetail = challengeService.getChallengeFindDetail(participationCode);
        } else {
            throw new IllegalArgumentException("챌린지 ID 또는 참여 코드를 입력하세요.");
        }
        return new ResponseEntity<>(
                ResponseDto.setSuccess(200, "챌린지 상세 조회 성공", challengeDetail),
                HttpStatus.OK);
    }

    @Operation(
            summary = "챌린지 생성",
            description = "새로운 챌린지를 생성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createChallenge(
            @Parameter(description = "챌린지 생성 요청 데이터", required = true)
            @RequestBody ChallengeCreateRequest request,
            @AuthUser UUID userId) {
        ResponseDto response = challengeService.createChallenge(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "챌린지 참여",
            description = "특정 챌린지에 참여합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping("/join/{challengeId}")
    public ResponseEntity<ResponseDto> challengeJoin(
            @PathVariable UUID challengeId,
            @RequestBody ChallengeJoinRequest request,
            @AuthUser UUID userId) throws BadRequestException {
        ChallengeDetailResponse challengeDetail = challengeService.joinChallenge(
                challengeId,
                userId,
                request.getParticipationCode()
        );
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 참여 성공", challengeDetail), HttpStatus.OK);
    }
}
