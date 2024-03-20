package com.project.foodfix.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class ImageService {

    // 매장 이미지가 저장될 경로를 설정합니다.
    @Value("${spring.upload.store.image.path}")
    private String storeImagePath;

    // 메뉴 이미지가 저장될 경로를 설정합니다.
    @Value("${spring.upload.menu.image.path}")
    private String menuImagePath;

    public String saveStoreImage(MultipartFile imageFile) throws IOException {
        return saveImage(imageFile, storeImagePath);
    }

    public String saveMenuImage(MultipartFile imageFile) throws IOException {
        return saveImage(imageFile, menuImagePath);
    }

    private String saveImage(MultipartFile imageFile, String imagePath) throws IOException {
        // 업로드된 파일의 이름을 정의합니다.
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));

        // UUID를 사용하여 파일명의 충돌을 방지합니다.
        fileName = UUID.randomUUID() + "-" + fileName;

        // 파일이 저장될 경로를 설정합니다.
        Path uploadDir = Paths.get(imagePath);

        // 파일이 저장될 디렉토리가 존재하지 않으면 생성합니다.
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 파일을 저장할 경로를 설정합니다.
        Path filePath = uploadDir.resolve(fileName);

        // 파일을 로컬 파일 시스템에 저장합니다.
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(imageFile.getBytes());
        }

        // 저장된 파일의 경로를 반환합니다.
        return imagePath + "/" + fileName;
    }
}