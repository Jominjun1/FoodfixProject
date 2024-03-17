package com.project.foodfix.controller;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.model.Admin;
import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Store;
import com.project.foodfix.repository.StoreRepository;
import com.project.foodfix.service.AuthService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    private final StoreRepository storeRepository;

    @Autowired
    public AdminController(AuthService authService, JwtTokenProvider jwtTokenProvider, StoreRepository storeRepository) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.storeRepository = storeRepository;
    }

    // 관리자 엔드 포인트
    // 관리자 정보 조회 API
    @GetMapping("/profile")
    public ResponseEntity<Object> getAdminProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponseObject();

        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) return ResponseEntity.ok(admin);

        return notFoundResponseObject();
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
    // 관리자 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        authService.logout(admin_id, UserType.ADMIN);
        return ResponseEntity.ok("로그아웃 성공");
    }
    // 관리자 수정 API
    @PutMapping("/update")
    public ResponseEntity<String> updateAdminInfo(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> updateInfo) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
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

        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // AuthService 통해 관리자 로그아웃 및 회원 탈퇴 처리
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            authService.logout(admin_id, UserType.ADMIN);
            authService.deleteUser(admin_id, UserType.ADMIN);
            return ResponseEntity.ok("관리자 회원 탈퇴 성공");
        }
        return notFoundResponse();
    }

    // 매장 엔드 포인트
    // 매장 조회 API
    @GetMapping("/allstores")
    public ResponseEntity<Object> getAllStores(@RequestHeader("Authorization") String authorizationHeader){
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponseObject();

        // 관리자가 소유한 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            return ResponseEntity.ok(admin.getStore());
        }

        return notFoundResponseObject();
    }
    // 매장 추가 API
    @PostMapping("/newstore")
    public ResponseEntity<String> createStore(@RequestBody Store store, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 목록에 새로운 매장 추가
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            store.setAdmin(admin);
            admin.setStore(store);
            authService.saveUser(admin);
            return ResponseEntity.ok("매장 등록 성공");
        }
        return notFoundResponse();
    }
    // 매장 수정 API
    @PutMapping("/updatestore")
    public ResponseEntity<String> updateStoreInfo(@RequestBody Map<String, String> updateInfo, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null && admin.getStore() != null) {
            // 매장 정보 업데이트
            Store store = admin.getStore();
            if (updateInfo.containsKey("store_name")) {
                store.setStore_name(updateInfo.get("store_name"));
            }
            if (updateInfo.containsKey("store_address")) {
                store.setStore_address(updateInfo.get("store_address"));
            }
            if (updateInfo.containsKey("store_category")) {
                store.setStore_category(updateInfo.get("store_category"));
            }
            if (updateInfo.containsKey("store_phone")) {
                store.setStore_phone(updateInfo.get("store_phone"));
            }
            if (updateInfo.containsKey("store_image")){
                store.setStore_image(updateInfo.get("store_image"));
            }
            if (updateInfo.containsKey("store_intro")){
                store.setStore_intro(updateInfo.get("store_intro"));
            }
            if (updateInfo.containsKey("minimumTime")) {
                store.setMinimumTime(Integer.valueOf(updateInfo.get("minimumTime")));
            }
            if (updateInfo.containsKey("res_status")) {
                store.setRes_status(updateInfo.get("res_status"));
            }
            if (updateInfo.containsKey("res_max")) {
                store.setRes_max(Integer.valueOf(updateInfo.get("res_max")));
            }
            if (updateInfo.containsKey("openTime")) {
                store.setOpenTime(LocalTime.parse(updateInfo.get("openTime")));
            }
            if (updateInfo.containsKey("closeTime")) {
                store.setCloseTime(LocalTime.parse(updateInfo.get("closeTime")));
            }
            if (updateInfo.containsKey("reservationCancel")) {
                store.setReservationCancel(LocalTime.parse(updateInfo.get("reservationCancel")));
            }
            // 업데이트된 정보 저장
            authService.saveUser(admin);
            return ResponseEntity.ok("매장 정보 수정 성공");
        }

        return notFoundResponse();
    }
    // 매장 삭제
    @DeleteMapping("/deletestore")
    public ResponseEntity<String> deleteStore(@RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 삭제
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            Store store = admin.getStore();
            if (store != null) {
                Long store_id = store.getStore_id();
                authService.deleteStore(store_id);
                return ResponseEntity.ok("매장 삭제 성공");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return notFoundResponse();
    }
    // 메뉴 엔드 포인트
    // 메뉴 조회 API
    @GetMapping("/menus")
    public ResponseEntity<Object> getMenusByStore(@RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponseObject();

        // 관리자가 소유한 매장 조회
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null && admin.getStore() != null) {
            // 매장의 모든 메뉴 조회
            List<Menu> menus = admin.getStore().getMenus();
            return ResponseEntity.ok(menus);
        }

        return notFoundResponseObject();
    }
    // 메뉴 추가 API
    @PostMapping("/newmenu")
    public ResponseEntity<String> createMenu(@RequestBody Menu newMenu, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null && admin.getStore() != null) {
            // 메뉴를 소유한 매장에 연결
            newMenu.setStore(admin.getStore());
            // 메뉴 저장
            authService.saveMenu(newMenu, admin_id);
            return ResponseEntity.ok("메뉴 등록 성공");
        }

        return notFoundResponse();
    }
    // 메뉴 수정 API
    @PutMapping("/updatemenu")
    public ResponseEntity<String> updateMenuInfo(@RequestBody Menu updatedMenu, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null && admin.getStore() != null) {
            // 매장에 속한 메뉴 중 수정할 메뉴 찾기
            List<Menu> menus = admin.getStore().getMenus();
            Menu newMenu = menus.stream()
                    .filter(menu -> menu.getMenu_id().equals(updatedMenu.getMenu_id()))
                    .findFirst()
                    .orElse(null);

            if (newMenu != null) {
                // 메뉴 정보 업데이트
                newMenu.setMenu_name(updatedMenu.getMenu_name());
                newMenu.setExplanation(updatedMenu.getExplanation());
                newMenu.setMenu_image(updatedMenu.getMenu_image());
                newMenu.setMenu_price(updatedMenu.getMenu_price());

                // 업데이트된 정보 저장
                authService.saveMenu(newMenu, admin_id);
                return ResponseEntity.ok("메뉴 정보 수정 성공");
            }
        }
        return notFoundResponse();
    }
    // 메뉴 삭제
    @DeleteMapping("/deletemenu/{menu_id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long menu_id , @RequestHeader("Authorization") String authorizationHeader){
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);

        // 매장이 소유한 메뉴인지 확인
        Menu menu = authService.findMenuById(menu_id);
        if (menu == null || !menu.getStore().getAdmin().equals(admin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        // 메뉴 삭제
        try {
            authService.deleteMenu(menu_id);
            return ResponseEntity.ok("메뉴 삭제 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제 실패");
        }
    }
    // 메서드 모음
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