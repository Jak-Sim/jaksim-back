package com.example.jaksim.user.service;

import com.example.jaksim.challenge.entity.UserChallenge;
import com.example.jaksim.challenge.repository.UserChallengeRepository;
import com.example.jaksim.user.dto.UserRecommendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.jaksim.common.s3.S3Service;
import com.example.jaksim.user.dto.UserUpdateDto;
import com.example.jaksim.user.repository.UserRepository;
import com.example.jaksim.user.entity.User;

import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final S3Service s3Service;

    @Autowired
    public UserService(UserRepository userRepository, UserChallengeRepository userChallengeRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.s3Service = s3Service;
    }

    
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User updateUser(UUID userUuid, UserUpdateDto userUpdateDto, MultipartFile profileImage) throws IOException {
        Optional<User> optionalUser = userRepository.findByUserUuid(userUuid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userUpdateDto.getUsername());
            user.setEmail(userUpdateDto.getEmail());

            if (profileImage != null) {
                String imageUrl = s3Service.upload(profileImage);
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserRecommendResponse findRelatedUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        List<UserChallenge> userChallenges = userChallengeRepository.findByUser(user);

        if (userChallenges.isEmpty()) {
            return UserRecommendResponse.builder()
                    .relatedUsers(List.of())
                    .count(0)
                    .build();
        }
        List<Long> challengeIds = userChallenges.stream()
                .map(uc -> uc.getChallenge().getChallengeId())
                .collect(Collectors.toList());
        List<UserChallenge> relatedUserChallenges = userChallengeRepository
                .findByChallengeChallengeIdInAndUserUserIdNot(challengeIds, userId);
        Set<Long> relatedUserIds = relatedUserChallenges.stream()
                .map(uc -> uc.getUser().getUserId())
                .collect(Collectors.toSet());

        if (relatedUserIds.isEmpty()) {
            return UserRecommendResponse.builder()
                    .relatedUsers(List.of())
                    .count(0)
                    .build();
        }
        List<User> relatedUsers = userRepository.findAllById(relatedUserIds);
        List<UserRecommendResponse.UserDto> userDtos = relatedUsers.stream()
                .map(relatedUser -> UserRecommendResponse.UserDto.builder()
                        .userId(relatedUser.getUserId())
                        .userUuid(relatedUser.getUserUuid())
                        .username(relatedUser.getUsername())
                        .email(relatedUser.getEmail())
                        .challengeIds(relatedUser.getChallengeIds())
                        .build())
                .collect(Collectors.toList());
        return UserRecommendResponse.builder()
                .relatedUsers(userDtos)
                .count(userDtos.size())
                .build();
    }
}
