package com.project.foodfix.service;

import com.project.foodfix.model.Photo;
import com.project.foodfix.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ImageService {

    @Value("${spring.upload.photo.images}")
    private String uploadDirPath; // 이미지가 저장될 디렉토리 경로
    private final PhotoRepository photoRepository; // Photo 엔티티를 저장하는 레포지토리

    @Autowired
    public ImageService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo saveImage(MultipartFile imageFile) throws IOException {
        // 업로드된 파일의 이름을 정의합니다.
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));

        // UUID 사용하여 파일명의 충돌을 방지합니다.
        String uniqueFileName = UUID.randomUUID() + "-" + fileName;

        // 파일이 저장될 경로를 설정합니다.
        Path uploadDir = Paths.get(uploadDirPath);

        // 파일이 저장될 디렉토리가 존재하지 않으면 생성합니다.
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 파일을 저장할 경로를 설정합니다.
        Path filePath = uploadDir.resolve(uniqueFileName);

        // 파일을 로컬 파일 시스템에 저장합니다.
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(imageFile.getBytes());
        }

        // 저장된 파일의 경로를 Photo 객체에 설정하여 반환합니다.
        Photo photo = new Photo();
        photo.setImagePath(filePath.toAbsolutePath().toString()); // 파일의 절대 경로를 설정

        // 저장된 파일의 정보를 DB에 저장합니다.
        photo = photoRepository.save(photo);
        return photo;
    }
}
