package com.example.jaksim.challenge.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() throws IOException {
        Files.createDirectories(fileStorageLocation);
    }

    public List<String> storeFiles(List<MultipartFile> files) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path targetLocation = fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation);
                fileUrls.add(targetLocation.toString());
            }
        }
        return fileUrls;
    }
}
