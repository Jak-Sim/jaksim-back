package com.example.jaksim.challenge.controller;

import com.example.jaksim.challenge.dto.challenge.ChallengeCreateRequest;
import com.example.jaksim.challenge.dto.challenge.ChallengeDetailResponse;
import com.example.jaksim.challenge.dto.challenge.ChallengeFindRequest;
import com.example.jaksim.challenge.dto.challenge.ChallengeListResponse;
import com.example.jaksim.challenge.service.ChallengeService;
import com.example.jaksim.common.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        @Parameter(description = "페이지 번호 (기본값: 1)", example = "1") 
        @RequestParam(defaultValue = "1") int page) {
        List<ChallengeListResponse> challenges = challengeService.getChallenges(page);     

        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 목록 조회 성공", challenges), HttpStatus.OK);
    }

    @Operation(summary = "챌린지 목록 조회", description = "유저에 따라 페이지를 기준으로 챌린지 목록을 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> getPersonalChallenges(
        @Parameter(description = "페이지 번호 (기본값: 1)", example = "1") 
        @RequestParam(defaultValue = "1") int page, @RequestParam String userUUID) {
        List<ChallengeListResponse> challenges = challengeService.getPersonalChallenges(page,userUUID);     

        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 목록 조회 성공", challenges), HttpStatus.OK);
    }

    @Operation(summary = "챌린지 상세 조회", description = "챌린지 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/{challengeId}")
    public ResponseEntity<ResponseDto> getChallengeDetail(
        @Parameter(description = "챌린지 ID", required = true, example = "1") 
        @PathVariable Long challengeId) {
        ChallengeDetailResponse challengeDetail = challengeService.getChallengeDetail(challengeId);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 상세 조회 성공", challengeDetail), HttpStatus.OK);
    }

    @Operation(summary = "챌린지 생성", description = "새로운 챌린지를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createChallenge(
        @Parameter(description = "챌린지 생성 요청 데이터", required = true) 
        @RequestBody ChallengeCreateRequest request,
        @Parameter(description = "JWT 인증 사용자 UUID", hidden = true) 
        @AuthenticationPrincipal String userUuid) {
        ResponseDto response = challengeService.createChallenge(request, userUuid);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "챌린지 찾기", description = "참여 코드를 통해 챌린지를 찾습니다.")
    @PostMapping("/find")
    public ResponseEntity<ResponseDto> findChallenge(
        @Parameter(description = "참여 코드 요청 데이터", required = true) 
        @RequestBody ChallengeFindRequest request) {
        ChallengeDetailResponse challengeDetail = challengeService.getChallengeFindDetail(request.getParticipationCode());
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 찾기 성공", challengeDetail), HttpStatus.OK);
    }

    @Operation(summary = "챌린지 코드 참여", description = "참여 코드를 통해 챌린지에 참가합니다..")
    @PostMapping("/join")
    public ResponseEntity<ResponseDto> challengeJoinByCode(
        @RequestBody Long challengeId,
        @RequestHeader("user-uuid") UUID userUuid
    ){
        ChallengeDetailResponse challengeDetail = challengeService.getChallengeJoin(challengeId, userUuid);
        return new ResponseEntity<>(ResponseDto.setSuccess(200, "챌린지 참여 성공", challengeDetail), HttpStatus.OK);
    }

}
