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
import java.util.Optional;

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
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponseObject();

        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) return ResponseEntity.ok(admin);

        return notFoundResponseObject();
    }
    // 관리자 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        authService.logout(adminId, UserType.ADMIN);
        return ResponseEntity.ok("로그아웃 성공");
    }
    // 관리자 수정 API
    @PutMapping("/update")
    public ResponseEntity<String> updateAdminInfo(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> updateInfo) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            // 수정할 정보 업데이트
            if (updateInfo.containsKey("admin_address")) {
                admin.setAdmin_address(updateInfo.get("admin_address"));
            }
            if (updateInfo.containsKey("admin_name")) {
                admin.setAdmin_name(updateInfo.get("admin_name"));
            }
            if (updateInfo.containsKey("admin_phone")) {
                admin.setAdmin_phone(updateInfo.get("admin_phone"));
            }
            if (updateInfo.containsKey("admin_pw")) {
                admin.setAdmin_pw(updateInfo.get("admin_pw"));
            }
            // 수정된 정보 저장
            authService.saveUser(admin);
            return ResponseEntity.ok("관리자 정보 수정 성공");
        }
        return notFoundResponse();
    }
    // 관리자 회원 탈퇴 API
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAdmin(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        // AuthService 통해 관리자 로그아웃 및 회원 탈퇴 처리
        authService.logout(adminId, UserType.ADMIN);
        authService.deleteUser(adminId, UserType.ADMIN);

        return ResponseEntity.ok("관리자 회원 탈퇴 성공");
    }
    // 토큰 추출 메서드
    private String extractToken(String authorizationHeader) {
        return Optional.ofNullable(authorizationHeader)
                .map(header -> header.replace("Bearer ", ""))
                .filter(jwtTokenProvider::validateToken)
                .orElse(null);
    }
    // 권한 없음 응답 ( Object )
    private ResponseEntity<Object> unauthorizedResponseObject() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
    }
    // 찾을 수 없음 응답 ( Object )
    private ResponseEntity<Object> notFoundResponseObject() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("관리자를 찾을 수 없습니다.");
    }
    // 권한 없음 응답
    private ResponseEntity<String> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
    }
    // 찾을 수 없음 응답
    private ResponseEntity<String> notFoundResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("관리자를 찾을 수 없습니다.");
    }
}