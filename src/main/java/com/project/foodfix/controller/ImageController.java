package com.project.foodfix.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.MalformedURLException;
import java.nio.file.*;

@Controller
public class ImageController {

    // 이미지 저장 경로
    private static final String DIRECTORY = "src/main/resources/static/images/";

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            // 경로 생성
            Path imagePath = Paths.get(DIRECTORY).resolve(imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            // 이미지 파일 존재 확인
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
