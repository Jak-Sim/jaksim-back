package com.example.jaksim.challenge.service;

import com.amazonaws.services.kms.model.NotFoundException;
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

import org.apache.coyote.BadRequestException;
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

	public ChallengeService(ChallengeRepository challengeRepository, UserRepository userRepository,
							MissionRepository missionRepository, UserChallengeRepository userChallengeRepository,
							RewardRepository rewardRepository, UsersTargetRewardsRepository usersTargetRewardsRepository) {
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
				.map(challenge -> ChallengeListResponse.ChallengeSummary.of(
						challenge.getChallengeId(),
						challenge.getName(),
						challenge.getBackgroundImage(),
						challenge.isPublic(),
						challenge.getCreatedUserId(),
						challenge.getCreatedAt()))
				.collect(Collectors.toList());

		return ChallengeListResponse.of(summaries);
	}

	// 챌린지 개인별 조회
	public Map<String, Object> getPersonalChallenges(int page, UUID userId) {
		List<UUID> challengeIds = userRepository.findByUserId(userId).get().getChallengeIds();
		Pageable pageable = PageRequest.of(page, 10);
		Page<Challenge> challengePage = challengeRepository.findByChallengeIdIn(challengeIds, pageable);

		Optional<User> optionalUser = userRepository.findByUserId(userId);
		if (optionalUser.isEmpty()) {
			throw new NullPointerException("유저를 찾을 수 없습니다.");
		}
		List<ChallengeListResponse.ChallengeSummary> summaries = challengePage.getContent().stream()
				.map(challenge -> {
					UUID challengeId = challenge.getChallengeId();
					int activeMissionsCount = getMissionCount(challengeId);
					int remainingPoint = calculateRemainingPoint(challengeId, userId);

					return new ChallengeListResponse.ChallengeSummary(
							challengeId,
							challenge.getName(),
							challenge.getBackgroundImage(),
							challenge.isPublic(),
							challenge.getCreatedUserId(),
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
	public ChallengeDetailResponse getChallengeDetail(UUID challengeId) {
		Challenge challenge = challengeRepository.findByChallengeId(challengeId);
		if (challenge == null) {
			throw new NotFoundException("챌린지를 찾을 수 없습니다.");
		}
		return buildChallengeDetailResponse(challenge);
	}

	@Transactional
	public ResponseDto createChallenge(ChallengeCreateRequest request, UUID userId) {
		// Validate request
		if (request.getName() == null || request.getName().isEmpty()) {
			throw new IllegalArgumentException("챌린지 이름은 필수입니다.");
		}

		if (request.getMaxParticipants() < 2 || request.getMaxParticipants() > 10) {
			throw new IllegalArgumentException("최대 참여자 수는 2-10명 입니다.");
		}

		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new NullPointerException("유저를 찾을 수 없습니다."));

		Challenge challenge = new Challenge();
		challenge.setName(request.getName());
		challenge.setBackgroundImage(request.getBackgroundImage());
		challenge.setPublic(request.isPublic());
		challenge.setMaxParticipants(request.getMaxParticipants());
		challenge.setTags(request.getTags());
		challenge.setCreatedUserId(userId);
		challenge.setParticipationCode(UUID.randomUUID().toString());
		challenge.setCurrentParticipants(1);

		Challenge savedChallenge = challengeRepository.save(challenge);

		UserChallenge userChallenge = new UserChallenge();
		userChallenge.setUser(user);
		userChallenge.setChallenge(savedChallenge);
		userChallenge.setPoints(0);
		userChallenge.setTotalPointsEarned(0);
		userChallenge.setTotalPointsSpent(0);
		userChallengeRepository.save(userChallenge);

		return new ResponseDto(201, "챌린지 생성 성공", Map.of(
				"challengeId", savedChallenge.getChallengeId(),
				"participationCode", savedChallenge.getParticipationCode()
		));
	}

	// 참여 코드로 조회
	public ChallengeDetailResponse getChallengeFindDetail(String participationCode) {
		Challenge challenge = challengeRepository.findByParticipationCode(participationCode);
		if (challenge == null) {
			throw new NotFoundException("챌린지를 찾을 수 없습니다.");
		}
		return buildChallengeDetailResponse(challenge);
	}

	public ChallengeDetailResponse joinChallenge(UUID challengeId, UUID userId, String participationCode) throws BadRequestException {
		return joinChallengeCommon(challengeId, userId, participationCode);
	}

	private ChallengeDetailResponse joinChallengeCommon(UUID challengeId, UUID userId, String participationCode) throws BadRequestException {
		Challenge challenge = challengeRepository.findByChallengeId(challengeId);
		if (challenge == null) {
			throw new NotFoundException("챌린지를 찾을 수 없습니다.");
		}

		User currentUser = userRepository.findByUserId(userId)
				.orElseThrow(() -> new BadRequestException("유저 정보를 찾을 수 없습니다."));

		Optional<UserChallenge> existingUserChallenge = userChallengeRepository.findByUserAndChallenge(currentUser, challenge);
		if (existingUserChallenge.isPresent()) {
			throw new BadRequestException("이미 참여한 챌린지입니다.");
		}

		if (!challenge.isPublic()) {
			if (participationCode == null || participationCode.trim().isEmpty() ||
					!participationCode.equals(challenge.getParticipationCode())) {
				throw new BadRequestException("비공개 챌린지거나 참여 코드가 맞지 않습니다.");
			}
		}

		int currentParticipants = userChallengeRepository.countUsersByChallengeChallengeId(challenge.getChallengeId());
		if (currentParticipants >= challenge.getMaxParticipants()) {
			throw new BadRequestException("최대 정원을 초과합니다. 참여할 수 없습니다.");
		}

		UserChallenge userChallenge = new UserChallenge();
		userChallenge.setUser(currentUser);
		userChallenge.setChallenge(challenge);
		userChallenge.setPoints(0);
		userChallengeRepository.save(userChallenge);

		List<UUID> challengeIds = currentUser.getChallengeIds();
		if (!challengeIds.contains(challenge.getChallengeId())) {
			challengeIds.add(challenge.getChallengeId());
			currentUser.setChallengeIds(challengeIds);
			userRepository.save(currentUser);
		}

		List<User> userList = userRepository.findUsersByChallengeId(challenge.getChallengeId());
		List<ParticipantResponse> participantsList = userList.stream()
				.map(user -> new ParticipantResponse(
						user.getUserId(),
						null,
						user.getUsername(),
						0
				))
				.collect(Collectors.toList());

		return new ChallengeDetailResponse(
				challenge.getChallengeId(),
				challenge.getName(),
				challenge.getBackgroundImage(),
				challenge.isPublic(),
				participantsList.size(),
				challenge.getMaxParticipants(),
				challenge.getTags(),
				challenge.getCreatedAt(),
				challenge.getUpdatedAt(),
				challenge.getCreatedUserId(),
				participantsList
		);
	}

	private ChallengeDetailResponse buildChallengeDetailResponse(Challenge challenge) {
		List<User> userList = userRepository.findUsersByChallengeId(challenge.getChallengeId());
		List<ParticipantResponse> participantsList = userList.stream()
				.map(user -> new ParticipantResponse(
						user.getUserId(),
						null,
						user.getUsername(),
						0
				))
				.toList();
		int currentParticipants = participantsList.size();

		return ChallengeDetailResponse.of(
				challenge.getChallengeId(),
				challenge.getName(),
				challenge.getBackgroundImage(),
				challenge.isPublic(),
				currentParticipants,
				challenge.getMaxParticipants(),
				challenge.getTags(),
				challenge.getCreatedAt(),
				challenge.getUpdatedAt(),
				challenge.getCreatedUserId(),
				participantsList
		);
	}

	private int getMissionCount(UUID challengeId) {
		return challengeRepository.findById(challengeId)
				.map(challenge -> missionRepository.countByChallengeAndIsActive(challenge, 1))
				.orElse(0);
	}

	private int calculateRemainingPoint(UUID challengeId, UUID userId) {
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new NullPointerException("유저를 찾을 수 없습니다."));
		Challenge challenge = challengeRepository.findByChallengeId(challengeId);
		if (challenge == null) {
			throw new NullPointerException("챌린지를 찾을 수 없습니다.");
		}
		UserChallenge userChallenge = userChallengeRepository.findByUserAndChallenge(user, challenge)
				.orElseThrow(() -> new NullPointerException("유저 챌린지 정보를 찾을 수 없습니다."));
		int currentPoints = userChallenge.getPoints();

		List<Reward> targetRewards = usersTargetRewardsRepository.findByUserIdAndIsActiveTrue(user.getUserId())
				.stream()
				.map(UsersTargetRewards::getReward)
				.filter(reward -> reward.getChallengeId().equals(challengeId))
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
		List<Reward> allRewards = rewardRepository.findByChallengeId(challenge.getChallengeId());
		if (allRewards.isEmpty()) {
			return 0;
		}
		Optional<Reward> nearestReward = allRewards.stream()
				.filter(reward -> reward.getRequiredPoints() > currentPoints)
				.min(Comparator.comparingInt(Reward::getRequiredPoints));
        return nearestReward.map(reward -> reward.getRequiredPoints() - currentPoints).orElse(0);
	}
}
