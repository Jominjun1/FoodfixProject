package com.project.foodfix.controller;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.model.Admin;
import com.project.foodfix.service.AuthService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AdminController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    // 관리자 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Admin admin) {
        return authService.signup(admin, UserType.ADMIN);
    }

    // 로그인 API
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody Admin loginRequest) {
        return authService.login(loginRequest.getAdmin_id(), loginRequest.getAdmin_pw(), UserType.ADMIN);
    }
    // 관리자 프로필 조회 API
    @GetMapping("/profile")
    public ResponseEntity<Object> getAdminProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(token)) {
            // 토큰에서 관리자 ID 추출
            String adminId = jwtTokenProvider.extractUserId(token);
            if (adminId != null) {
                // AuthService 통해 관리자 정보 조회
                Object admin = authService.getUser(adminId, UserType.ADMIN);
                if (admin != null) {
                    return ResponseEntity.ok(admin);
                } else {
                    System.out.println("데이터베이스에서 관리자를 못찾음");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                System.out.println("토큰에서 관리자 ID를 추출하지 못함");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            System.out.println("\n" + "토큰 검증에 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // 관리자 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtTokenProvider.validateToken(token)) {
            String adminId = jwtTokenProvider.extractUserId(token);
            if (adminId != null) {
                // AuthService 통해 관리자 로그아웃 처리
                authService.logout(adminId, UserType.ADMIN);
                return ResponseEntity.ok("로그아웃 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
        }
    }

}