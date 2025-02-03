package com.example.jaksim.challenge.service;

import com.example.jaksim.challenge.dto.challenge.ChallengeCreateRequest;
import com.example.jaksim.challenge.dto.challenge.ChallengeDetailResponse;
import com.example.jaksim.challenge.dto.challenge.ChallengeListResponse;
import com.example.jaksim.challenge.dto.challenge.ParticipantResponse;
import com.example.jaksim.challenge.entity.Challenge;
import com.example.jaksim.challenge.repository.ChallengeRepository;
import com.example.jaksim.common.ResponseDto;
import com.example.jaksim.user.entity.User;
import com.example.jaksim.user.repository.UserRepository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

	private final ChallengeRepository challengeRepository;
	private final UserRepository userRepository;

	public ChallengeService(ChallengeRepository challengeRepository, UserRepository userRepository) {
		this.challengeRepository = challengeRepository;
		this.userRepository = userRepository;
	}

	// 챌린지 목록 조회
	public List<ChallengeListResponse> getChallenges(int page) {
		Pageable pageable = PageRequest.of(page, 10); // 페이지당 10개 챌린지
		Page<Challenge> challengePage = challengeRepository.findAll(pageable);

		return challengePage.getContent().stream()
			.map(challenge -> new ChallengeListResponse(
				challenge.getChallengeId(),
				challenge.getName(),
				challenge.getBackgroundImage(),
				challenge.getCreatorUuid(),
				challenge.getCreatedAt()))
			.collect(Collectors.toList());
	}

	// 챌린지 개인별 조회
	public List<ChallengeListResponse> getPersonalChallenges(int page, String userUuid){
		Pageable pageable = PageRequest.of(page, 10); 
		
		List<Long> challengeIds = userRepository.findByUserUuid(UUID.fromString(userUuid)).get().getChallengeIds();

		Page<Challenge> challengePage = challengeRepository.findByChallengeIdIn(challengeIds, pageable);
		return challengePage.getContent().stream()
			.map(challenge -> new ChallengeListResponse(
				challenge.getChallengeId(),
				challenge.getName(),
				challenge.getBackgroundImage(),
				challenge.getCreatorUuid(),
				challenge.getCreatedAt()))
			.collect(Collectors.toList());
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
		challenge.setCreatorUuid(String.valueOf(UUID.fromString(userUuid))); // JWT에서 받은 userUuid 사용
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
		if(!optionalUser.isPresent()){
			throw new NullPointerException("유저 정보가 잘못되었습니다.");
		}
		
		User currentUser = optionalUser.get();

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
				challenge.getCreatorUuid().toString(),
				participantsList
				);
	}
	
}
