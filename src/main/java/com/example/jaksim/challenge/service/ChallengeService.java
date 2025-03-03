package com.example.jaksim.challenge.service;

import com.example.jaksim.challenge.dto.challenge.ChallengeCreateRequest;
import com.example.jaksim.challenge.dto.challenge.ChallengeDetailResponse;
import com.example.jaksim.challenge.dto.challenge.ChallengeListResponse;
import com.example.jaksim.challenge.dto.challenge.ParticipantResponse;
import com.example.jaksim.challenge.entity.*;
import com.example.jaksim.challenge.repository.*;
import com.example.jaksim.common.ResponseDto;
import com.example.jaksim.user.entity.User;
import com.example.jaksim.user.repository.UserRepository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class ChallengeService {

	private final ChallengeRepository challengeRepository;
	private final UserRepository userRepository;
	private final MissionRepository missionRepository;
	private final UserChallengeRepository userChallengeRepository;
	private final RewardRepository rewardRepository;
	private final UsersTargetRewardsRepository usersTargetRewardsRepository;

	public ChallengeService(ChallengeRepository challengeRepository, UserRepository userRepository, MissionRepository missionRepository, UserChallengeRepository userChallengeRepository, RewardRepository rewardRepository, UsersTargetRewardsRepository usersTargetRewardsRepository) {
		this.challengeRepository = challengeRepository;
		this.userRepository = userRepository;
        this.missionRepository = missionRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.rewardRepository = rewardRepository;
        this.usersTargetRewardsRepository = usersTargetRewardsRepository;
    }

	// 챌린지 목록 조회
	public ChallengeListResponse getChallenges(int page) {
		Pageable pageable = PageRequest.of(page, 10); // 페이지당 10개 챌린지
		Page<Challenge> challengePage = challengeRepository.findAll(pageable);

		List<ChallengeListResponse.ChallengeSummary> summaries = challengePage.getContent().stream()
				.map(challenge -> new ChallengeListResponse.ChallengeSummary(
						challenge.getChallengeId(),
						challenge.getName(),
						challenge.getBackgroundImage(),
						challenge.isPublic(),
						challenge.getCreatorUuid(),
						challenge.getCreatedAt()))
				.collect(Collectors.toList());

		return new ChallengeListResponse(summaries);
	}

	// 챌린지 개인별 조회
	public Map<String, Object> getPersonalChallenges(int page, String userUuid) {
		List<Long> challengeIds = userRepository.findByUserUuid(UUID.fromString(userUuid)).get().getChallengeIds();
		Pageable pageable = PageRequest.of(page, 10);
		Page<Challenge> challengePage = challengeRepository.findByChallengeIdIn(challengeIds, pageable);

		Optional<User> optionalUser = userRepository.findByUserUuid(UUID.fromString(userUuid));
		if (optionalUser.isEmpty()) {
			throw new NullPointerException("유저를 찾을 수 없습니다.");
		}
		List<ChallengeListResponse.ChallengeSummary> summaries = challengePage.getContent().stream()
				.map(challenge -> {
					Long challengeId = challenge.getChallengeId();
					int activeMissionsCount = getMissionCount(challengeId);
					int remainingPoint = calculateRemainingPoint(challengeId, userUuid);

					return new ChallengeListResponse.ChallengeSummary(
							challengeId,
							challenge.getName(),
							challenge.getBackgroundImage(),
							challenge.isPublic(),
							challenge.getCreatorUuid(),
							challenge.getCreatedAt(),
							remainingPoint,
							activeMissionsCount,
							challenge.getCurrentParticipants()
					);
				})
				.collect(Collectors.toList());
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("challenges", summaries);
		responseData.put("pageInfo", Map.of(
				"currentPage", challengePage.getNumber(),
				"totalPages", challengePage.getTotalPages(),
				"totalElements", challengePage.getTotalElements(),
				"size", challengePage.getSize()
		));
		return responseData;
	}

	// 챌린지 상세 조회
	public ChallengeDetailResponse getChallengeDetail(Long challengeId) {
		Challenge challenge = challengeRepository.findByChallengeId(challengeId);
		if (challenge == null) {
			throw new NullPointerException("챌린지를 찾을 수 없습니다."); // Custom Exception
		}

		List<User> userList = userRepository.findUsersByChallengeId(challenge.getChallengeId());
			

		int currentParticipants = userList.size();

		return new ChallengeDetailResponse(
			challenge.getChallengeId(),
			challenge.getName(),
			challenge.getBackgroundImage(),
			challenge.isPublic(),
			currentParticipants,
			challenge.getMaxParticipants(),
			challenge.getTags(),
			challenge.getCreatedAt(),
			challenge.getUpdatedAt(),
			challenge.getCreatorUuid()
			);
	}

	// 챌린지 생성
	@Transactional
	public ResponseDto createChallenge(ChallengeCreateRequest request, String userUuid) {
		// Validate request
		if (request.getName() == null || request.getName().isEmpty()) {
			throw new IllegalArgumentException("챌린지 이름은 필수입니다.");
		}

		Challenge challenge = new Challenge();
		challenge.setName(request.getName());
		challenge.setBackgroundImage(request.getBackgroundImage());
		challenge.setPublic(request.isPublic());
		challenge.setMaxParticipants(request.getMaxParticipants());
		challenge.setTags(request.getTags());
		challenge.setCreatorUuid(userUuid); // JWT에서 받은 userUuid 사용
		challenge.setParticipationCode(generateParticipationCode()); // Generate participation code

		Challenge savedChallenge = challengeRepository.save(challenge);

		return new ResponseDto(201, "챌린지 생성 성공", Map.of(
			"challengeId", savedChallenge.getChallengeId(),
			"participationCode", savedChallenge.getParticipationCode()
		));
	}

	private String generateParticipationCode() {

		return UUID.randomUUID().toString();
	}


	// 챌린지 상세 조회
	public ChallengeDetailResponse getChallengeFindDetail(String participationCode) {
			Challenge challenge = challengeRepository.findByParticipationCode(participationCode);
			if (challenge == null) {
				throw new NullPointerException("챌린지를 찾을 수 없습니다."); 
			}

			List<User> userList = userRepository.findUsersByChallengeId(challenge.getChallengeId());
			List<ParticipantResponse> participantsList = userList.stream()
								.map(user -> new ParticipantResponse(
										user.getUserUuid().toString(),  
										null,
										user.getUsername(),
										0            
								))
								.toList();

			int currentParticipants = participantsList.size();
			
			return new ChallengeDetailResponse(
				challenge.getChallengeId(),
				challenge.getName(),
				challenge.getBackgroundImage(),
				challenge.isPublic(),
				currentParticipants,
				challenge.getMaxParticipants(),
				challenge.getTags(),
				challenge.getCreatedAt(),
				challenge.getUpdatedAt(),
				challenge.getCreatorUuid()
			);
		}

	// 챌린지 코드로 참여
	public ChallengeDetailResponse getChallengeJoin(Long challengeId, UUID userUuid){
		Challenge challenge = challengeRepository.findByChallengeId(challengeId);
			if (challenge == null) {
				throw new NullPointerException("챌린지를 찾을 수 없습니다."); 
			}
		
		Optional<User> optionalUser = userRepository.findByUserUuid(userUuid);
		if(optionalUser.isEmpty()){
			throw new NullPointerException("유저 정보가 잘못되었습니다.");
		}
		
		User currentUser = optionalUser.get();

		UserChallenge userChallenge = userChallengeRepository.findByUserAndChallenge(currentUser, challenge)
				.orElseGet(() -> {
					UserChallenge newUserChallenge = new UserChallenge();
					newUserChallenge.setUser(currentUser);
					newUserChallenge.setChallenge(challenge);
					newUserChallenge.setPoints(0);
					return userChallengeRepository.save(newUserChallenge);
				});

		List<Long> challengeIds = currentUser.getChallengeIds();
		if(!challengeIds.contains(challenge.getChallengeId())){
			challengeIds.add(challenge.getChallengeId());
			currentUser.setChallengeIds(challengeIds);
			userRepository.save(currentUser);
		}


		List<User> userList = userRepository.findUsersByChallengeId(challenge.getChallengeId());
		List<ParticipantResponse> participantsList = userList.stream()
								.map(user -> new ParticipantResponse(
										user.getUserUuid().toString(),  
										null,
										user.getUsername(),
										0
								))
								.collect(Collectors.toList());

		int currentParticipants = participantsList.size();

		return new ChallengeDetailResponse(
				challenge.getChallengeId(),
				challenge.getName(),
				challenge.getBackgroundImage(),
				challenge.isPublic(),
				currentParticipants,
				challenge.getMaxParticipants(),
				challenge.getTags(),
				challenge.getCreatedAt(),
				challenge.getUpdatedAt(),
                challenge.getCreatorUuid(),
				participantsList
				);
	}

	private int getMissionCount(Long challengeId) {
		return challengeRepository.findById(challengeId)
				.map(challenge -> missionRepository.countByChallengeAndIsActive(challenge, 1))
				.orElse(0);
	}

	private int calculateRemainingPoint(Long challengeId, String userUuid) {
		User user = userRepository.findByUserUuid(UUID.fromString(userUuid))
				.orElseThrow(() -> new NullPointerException("유저를 찾을 수 없습니다."));
		Challenge challenge = challengeRepository.findByChallengeId(challengeId);
		if (challenge == null) {
			throw new NullPointerException("챌린지를 찾을 수 없습니다.");
		}
		UserChallenge userChallenge = userChallengeRepository.findByUserAndChallenge(user, challenge)
				.orElseThrow(() -> new NullPointerException("유저 챌린지 정보를 찾을 수 없습니다."));
		int currentPoints = userChallenge.getPoints();

		List<Reward> targetRewards = usersTargetRewardsRepository.findByUserIdAndIsActiveTrue(user.getUserUuid())
				.stream()
				.map(UsersTargetRewards::getReward)
				.filter(reward -> reward.getChallenge().getChallengeId().equals(challengeId))
				.toList();

		if (targetRewards.isEmpty()) {
			return findRewardsPoint(challenge, currentPoints);
		}
		Optional<Reward> nearestReward = targetRewards.stream()
				.filter(reward -> reward.getRequiredPoints() > currentPoints)
				.min(Comparator.comparingInt(reward -> reward.getRequiredPoints() - currentPoints));
        return nearestReward.map(reward -> reward.getRequiredPoints() - currentPoints).orElse(0);
	}

	private int findRewardsPoint(Challenge challenge, int currentPoints) {
		List<Reward> allRewards = rewardRepository.findByChallenge(challenge);
		if (allRewards.isEmpty()) {
			return 0;
		}
		Optional<Reward> nearestReward = allRewards.stream()
				.filter(reward -> reward.getRequiredPoints() > currentPoints)
				.min(Comparator.comparingInt(Reward::getRequiredPoints));
        return nearestReward.map(reward -> reward.getRequiredPoints() - currentPoints).orElse(0);
	}
}
