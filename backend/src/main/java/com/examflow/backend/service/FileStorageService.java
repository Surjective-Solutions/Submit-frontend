package com.examflow.backend.service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = System.getProperty("user.home")
            + "/lms/uploads/papers/";

    public String savePaperFile(MultipartFile file) {
        try {

            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalName = file.getOriginalFilename();
            String uniqueName = System.currentTimeMillis() + "_" + originalName;

            String filePath = UPLOAD_DIR + uniqueName;

            file.transferTo(new File(filePath));

            return uniqueName;

        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }

    }
}
