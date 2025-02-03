package com.example.jaksim.challenge.service;

import com.example.jaksim.challenge.dto.mission.MissionDto;
import com.example.jaksim.challenge.entity.Mission;
import com.example.jaksim.challenge.repository.MissionRepository;
import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final ChallengeRepository challengeRepository;

    @Autowired
    public MissionService(MissionRepository missionRepository, ChallengeRepository challengeRepository) {
        this.missionRepository = missionRepository;
        this.challengeRepository = challengeRepository;
    }

    // 챌린지에 속한 모든 미션 조회
    public List<MissionDto> getMissions(Long challengeId) {
        List<Mission> missions = missionRepository.findByChallengeChallengeId(challengeId);
        return missions.stream()
                .map(mission -> {
                    MissionDto missionDto = new MissionDto();
                    missionDto.setMissionId(mission.getMissionId());
                    missionDto.setTitle(mission.getTitle());
                    missionDto.setContent(mission.getContent());
                    missionDto.setCompletionPoints(mission.getCompletionPoints());
                    return missionDto;
                })
                .collect(Collectors.toList());
    }

    // 특정 미션 조회
    public MissionDto getMission(Long challengeId, Long missionId) {
        Mission mission = missionRepository.findByIdAndChallengeChallengeId(missionId, challengeId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));
        MissionDto missionDto = new MissionDto();
        missionDto.setMissionId(mission.getMissionId());
        missionDto.setTitle(mission.getTitle());
        missionDto.setContent(mission.getContent());
        missionDto.setCompletionPoints(mission.getCompletionPoints());
        return missionDto;
    }

    // 미션 생성
    @Transactional
    public MissionDto createMission(Long challengeId, MissionDto missionDto) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));

        Mission mission = new Mission();
        mission.setChallenge(challenge);
        mission.setTitle(missionDto.getTitle());
        mission.setContent(missionDto.getContent());
        mission.setCompletionPoints(missionDto.getCompletionPoints());
        mission.setStartDate(missionDto.getStartDate());
        mission.setEndDate(missionDto.getEndDate());
        mission.setBackgroundImages(missionDto.getBackgroundImages());
        mission.setCompletionDeadline(missionDto.getCompletionDeadline());

        missionRepository.save(mission);

        // 미션 저장 후 DTO 반환
        missionDto.setMissionId(mission.getMissionId());
        return missionDto;
    }

    // 미션 삭제
    @Transactional
    public void deleteMission(Long challengeId, Long missionId) {
        Mission mission = missionRepository.findByIdAndChallengeChallengeId(missionId, challengeId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));
        missionRepository.delete(mission);
    }
}
