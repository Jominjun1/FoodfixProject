package com.project.foodfix.controller;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.model.*;
import com.project.foodfix.model.DTO.MenuDTO;
import com.project.foodfix.model.DTO.PackingDTO;
import com.project.foodfix.model.DTO.ReservationDTO;
import com.project.foodfix.repository.StoreRepository;
import com.project.foodfix.service.AuthService;
import com.project.foodfix.service.Store.StoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
@Controller
public class UserController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final StoreRepository storeRepository;
    private final StoreServiceImpl storeServiceImpl;

    @Autowired
    public UserController(AuthService authService, JwtTokenProvider jwtTokenProvider, StoreRepository storeRepository, StoreServiceImpl storeServiceImpl) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.storeRepository = storeRepository;
        this.storeServiceImpl = storeServiceImpl;
    }
    // 사용자 프로필 조회 API
    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(token)) {
            // 토큰에서 사용자 ID 추출
            String user_id = jwtTokenProvider.extractUserId(token);
            if (user_id != null) {

                Object user = authService.getUser(user_id, UserType.USER);
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
    // 매장의 메뉴 조회
    @GetMapping("/menus/{store_id}")
    public ResponseEntity<Object> getMenus(@PathVariable Long store_id) {
        Optional<Store> optionalStore = storeRepository.findById(store_id);

        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            List<Menu> menus = store.getMenus();

            List<MenuDTO> menuDTOs = new ArrayList<>();
            for (Menu menu : menus) {
                String imagePath = menu.getMenuPhoto() != null ? menu.getMenuPhoto().getImagePath() : null;
                MenuDTO menuDTO = new MenuDTO(
                        menu.getMenu_id(),
                        menu.getMenu_price(),
                        menu.getMenu_name(),
                        menu.getExplanation(),
                        imagePath
                );
                menuDTOs.add(menuDTO);
            }
            return ResponseEntity.ok(menuDTOs);
        } else {
            return ResponseEntity.ok("매장 정보 조회 실패");
        }
    }
    // 예약 내역 조회
    @GetMapping("/reservations")
    public ResponseEntity<Object> getUserReservations(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String user_id = jwtTokenProvider.extractUserId(token);

        User user = (User) authService.getUser(user_id, UserType.USER);
        if (user != null) {
            List<ReservationDTO> reservationDTOs = new ArrayList<>();
            List<Reservation> userReservations = user.getReservations();
            for(Reservation reservation : userReservations) {
                ReservationDTO reservationDTO = storeServiceImpl.ReservationList(reservation);
                reservationDTOs.add(reservationDTO);
            }
            return ResponseEntity.ok(reservationDTOs);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    // 포장 내역 조회
    @GetMapping("/packings")
    public ResponseEntity<Object> getUserPackings(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String user_id = jwtTokenProvider.extractUserId(token);

        User user = (User) authService.getUser(user_id, UserType.USER);
        if (user != null) {
            List<PackingDTO> packingDTOs = new ArrayList<>();
            List<Packing> userPackings = user.getPackings();
            for (Packing packing : userPackings) {
                PackingDTO packingDTO = storeServiceImpl.PackingList(packing);
                packingDTOs.add(packingDTO);
            }
            return ResponseEntity.ok(packingDTOs);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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
    public ResponseEntity<String> updateUserInfo(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> updateInfo) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String user_id = jwtTokenProvider.extractUserId(token);
        if (user_id == null) return unauthorizedResponse();

        User user = (User) authService.getUser(user_id, UserType.USER);
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
                user.setUser_pw(updateInfo.get("user_pw"));
            }
            if (updateInfo.containsKey("nickname")) {
                user.setNickname(updateInfo.get("nickname"));
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

        String user_id = jwtTokenProvider.extractUserId(token);
        if (user_id == null) return unauthorizedResponse();

        // AuthService 통해 사용자 로그아웃 및 회원 탈퇴 처리
        authService.logout(user_id, UserType.USER);
        authService.deleteUser(user_id, UserType.USER);
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