package com.project.foodfix.controller;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.model.Admin;
import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Store;
import com.project.foodfix.service.AuthService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AdminController(AuthService authService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
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
    // 매장 조회 API
    @GetMapping("/allstores")
    public ResponseEntity<Object> getAllStores(@RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponseObject();

        // 관리자가 소유한 매장 목록 조회
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            List<Store> stores = admin.getStores();
            return ResponseEntity.ok(stores);
        }

        return notFoundResponseObject();
    }
    // 메뉴 조회 API
    @GetMapping("/store/allmenus/{storeId}")
    public ResponseEntity<String> getMenusOfStore(@PathVariable Long storeId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            Optional<Store> optionalStore = admin.getStores().stream().filter(store -> store.getStore_id().equals(storeId)).findFirst();
            if (optionalStore.isPresent()) {
                List<Menu> menus = optionalStore.get().getMenus();
                return ResponseEntity.ok(menus.toString());
            }
        }
        return notFoundResponse();
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
    // 매장 추가 API
    @PostMapping("/newstore")
    public ResponseEntity<String> createStore(@RequestBody Store store, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 목록에 새로운 매장 추가
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            store.setAdmin(admin);
            admin.getStores().add(store);
            authService.saveUser(admin);
            return ResponseEntity.ok("매장 등록 성공");
        }

        return notFoundResponse();
    }
    // 메뉴 추가 API
    @PostMapping("/store/newmenu/{storeId}")
    public ResponseEntity<String> addMenuToStore(@PathVariable Long storeId, @RequestBody Menu menu, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            Optional<Store> optionalStore = admin.getStores().stream().filter(store -> store.getStore_id().equals(storeId)).findFirst();
            if (optionalStore.isPresent()) {
                Store store = optionalStore.get();
                menu.setStore(store);
                store.getMenus().add(menu);
                authService.saveUser(admin);
                return ResponseEntity.ok("메뉴 추가 성공");
            }
        }

        return notFoundResponse();
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
                String hashedPassword = passwordEncoder.encode(updateInfo.get("admin_pw"));
                admin.setAdmin_pw(hashedPassword);
            }
            // 수정된 정보 저장
            authService.saveUser(admin);
            return ResponseEntity.ok("관리자 정보 수정 성공");
        }
        return notFoundResponse();
    }
    // 매장 수정 API
    @PutMapping("/store/{storeId}")
    public ResponseEntity<String> updateStoreInfo(@PathVariable Long storeId, @RequestBody Map<String, String> updateInfo, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 목록에서 특정 매장 조회
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            Optional<Store> optionalStore = admin.getStores().stream().filter(store -> store.getStore_id().equals(storeId)).findFirst();
            if (optionalStore.isPresent()) {
                // 매장 정보 업데이트
                Store store = optionalStore.get();
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
        }

        return notFoundResponse();
    }
    // 메뉴 수정 API
    @PutMapping("/updatemenu/{storeId}/{menuId}")
    public ResponseEntity<String> updateMenuOfStore(@PathVariable Long storeId,  @PathVariable Long menuId, @RequestBody Menu updatedMenu, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            Optional<Store> optionalStore = admin.getStores().stream().filter(store -> store.getStore_id().equals(storeId)).findFirst();
            if (optionalStore.isPresent()) {
                Optional<Menu> optionalMenu = optionalStore.get().getMenus().stream().filter(menu -> menu.getMenu_id().equals(menuId)).findFirst();
                if (optionalMenu.isPresent()) {
                    Menu existingMenu = optionalMenu.get();
                    // 원하는 속성만 업데이트
                    if (updatedMenu.getMenu_name() != null) {
                        existingMenu.setMenu_name(updatedMenu.getMenu_name());
                    }
                    if (updatedMenu.getExplanation() != null) {
                        existingMenu.setExplanation(updatedMenu.getExplanation());
                    }
                    if (updatedMenu.getMenu_image() != null) {
                        existingMenu.setMenu_image(updatedMenu.getMenu_image());
                    }
                    if (updatedMenu.getMenu_price() != null) {
                        existingMenu.setMenu_price(updatedMenu.getMenu_price());
                    }
                    authService.saveUser(admin);
                    return ResponseEntity.ok("메뉴 수정 성공");
                }
            }
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
    // 매장 삭제 API
    @DeleteMapping("/delstore/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        // 관리자가 소유한 매장 목록에서 특정 매장 삭제
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            Optional<Store> optionalStore = admin.getStores().stream().filter(store -> store.getStore_id().equals(storeId)).findFirst();
            if (optionalStore.isPresent()) {
                // 매장 삭제
                admin.getStores().remove(optionalStore.get());
                authService.saveUser(admin);
                return ResponseEntity.ok("매장 삭제 성공");
            }
        }

        return notFoundResponse();
    }
    // 메뉴 삭제 API
    @DeleteMapping("/delmenu/{storeId}/{menuId}")
    public ResponseEntity<String> deleteMenuOfStore(@PathVariable Long storeId, @PathVariable Long menuId, @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null) {
            Optional<Store> optionalStore = admin.getStores().stream().filter(store -> store.getStore_id().equals(storeId)).findFirst();
            if (optionalStore.isPresent()) {
                Optional<Menu> optionalMenu = optionalStore.get().getMenus().stream().filter(menu -> menu.getMenu_id().equals(menuId)).findFirst();
                if (optionalMenu.isPresent()) {
                    // 메뉴 삭제 로직 추가
                    optionalStore.get().getMenus().remove(optionalMenu.get());
                    authService.saveUser(admin);
                    return ResponseEntity.ok("메뉴 삭제 성공");
                }
            }
        }

        return notFoundResponse();
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