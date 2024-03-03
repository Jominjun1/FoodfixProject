package com.project.foodfix.controller;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.model.User;
import com.project.foodfix.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
@Controller
public class UserController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AuthService authService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }
    // 사용자 프로필 조회 API
    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(token)) {
            // 토큰에서 사용자 ID 추출
            String userId = jwtTokenProvider.extractUserId(token);
            if (userId != null) {
                // AuthService 통해 사용자 정보 조회
                Object user = authService.getUser(userId, UserType.USER);
                if (user != null) {
                    return ResponseEntity.ok(user);
                } else {
                    System.out.println("데이터베이스에서 사용자를 못찾음");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                System.out.println("토큰에서 사용자 ID를 추출하지 못함");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            System.out.println("\n" + "토큰 검증에 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    // 사용자 로그인 API
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest) {
        return authService.login(loginRequest.getUser_id(), loginRequest.getUser_pw(), UserType.USER);
    }
    // 사용자 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        return authService.signup(user, UserType.USER);
    }
    // 사용자 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.extractUserId(token);
            if (userId != null) {
                // AuthService 통해 사용자 로그아웃 처리
                authService.logout(userId, UserType.USER);
                return ResponseEntity.ok("로그아웃 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
        }
    }
    // 사용자 정보 수정 API
    @PutMapping("/update")
    public ResponseEntity<String> updateAdminInfo(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> updateInfo) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String userId = jwtTokenProvider.extractUserId(token);
        if (userId == null) return unauthorizedResponse();

        User user = (User) authService.getUser(userId, UserType.ADMIN);
        if (user != null) {
            // 수정할 정보 업데이트
            if (updateInfo.containsKey("user_address")) {
                user.setUser_address(updateInfo.get("user_address"));
            }
            if (updateInfo.containsKey("user_name")) {
                user.setUser_name(updateInfo.get("user_name"));
            }
            if (updateInfo.containsKey("user_phone")) {
                user.setUser_phone(updateInfo.get("user_phone"));
            }
            if (updateInfo.containsKey("user_pw")) {
                String hashedPassword = passwordEncoder.encode(updateInfo.get("user_pw"));
                user.setUser_pw(hashedPassword);
            }
            // 수정된 정보 저장
            authService.saveUser(user);
            return ResponseEntity.ok("유저 정보 수정 성공");
        }
        return notFoundResponse();
    }
    // 사용자 회원 탈퇴 API
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String userId = jwtTokenProvider.extractUserId(token);
        if (userId == null) return unauthorizedResponse();

        // AuthService 통해 사용자 로그아웃 및 회원 탈퇴 처리
        authService.logout(userId, UserType.USER);
        authService.deleteUser(userId, UserType.USER);

        return ResponseEntity.ok("사용자 회원 탈퇴 성공");
    }
    // 토큰 추출 메서드
    private String extractToken(String authorizationHeader) {
        return Optional.ofNullable(authorizationHeader)
                .map(header -> header.replace("Bearer ", ""))
                .filter(jwtTokenProvider::validateToken)
                .orElse(null);
    }
    // 권한 없음 응답
    private ResponseEntity<String> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
    }
    // 찾을 수 없음 응답
    private ResponseEntity<String> notFoundResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
    }
}