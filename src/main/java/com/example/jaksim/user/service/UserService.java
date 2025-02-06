package com.example.jaksim.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.jaksim.common.s3.S3Service;
import com.example.jaksim.user.dto.UserUpdateDto;
import com.example.jaksim.user.repository.UserRepository;
import com.example.jaksim.user.entity.User;

import java.util.UUID;
import java.io.IOException;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Autowired
    public UserService(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
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
}
