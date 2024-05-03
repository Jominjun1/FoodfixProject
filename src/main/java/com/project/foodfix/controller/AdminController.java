package com.project.foodfix.controller;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.config.WebSocketHandler;
import com.project.foodfix.model.Admin;
import com.project.foodfix.model.DTO.MenuDTO;
import com.project.foodfix.model.DTO.PackingDTO;
import com.project.foodfix.model.DTO.ReservationDTO;
import com.project.foodfix.model.DTO.StoreDTO;
import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Photo;
import com.project.foodfix.model.Store;
import com.project.foodfix.repository.MenuRepository;
import com.project.foodfix.service.AuthService;
import com.project.foodfix.service.ImageService;
import com.project.foodfix.service.Store.StoreService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final AuthService authService;
    private final StoreService storeService;
    private final ImageService imageService;
    private final MenuRepository menuRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final WebSocketHandler webSocketHandler;

    @Autowired
    public AdminController(AuthService authService, StoreService storeService, ImageService imageService, MenuRepository menuRepository, JwtTokenProvider jwtTokenProvider, WebSocketHandler webSocketHandler) {
        this.authService = authService;
        this.storeService = storeService;
        this.imageService = imageService;
        this.menuRepository = menuRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.webSocketHandler = webSocketHandler;
    }
    //*********** 관리자 엔드 포인트 **************//
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

        // 관리자 로그아웃 및 회원 탈퇴 처리
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            authService.logout(admin_id, UserType.ADMIN);
            authService.deleteUser(admin_id, UserType.ADMIN);
            return ResponseEntity.ok("관리자 회원 탈퇴 성공");
        }
        return notFoundResponse();
    }

    //********** 매장 엔드 포인트 **********//
    // 매장 조회 API
    @GetMapping("/store")
    public ResponseEntity<Object> getAllStores(@RequestHeader("Authorization") String authorizationHeader){
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponseObject();

        //  매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            Store store = admin.getStore();
            if (store != null) {
                String imagePath = store.getPhoto() != null ? store.getPhoto().getImagePath() : null;
                StoreDTO storeDTO = new StoreDTO(
                        store.getStore_id(),
                        store.getStore_name(),
                        store.getStore_address(),
                        store.getStore_category(),
                        store.getStore_phone(),
                        store.getRes_status(),
                        store.getStore_intro(),
                        imagePath,
                        store.getMinimumTime(),
                        store.getRes_max(),
                        store.getOpenTime(),
                        store.getCloseTime(),
                        store.getReservationCancel()
                );
                return ResponseEntity.ok(storeDTO);
            } else {
                return notFoundResponseObject();
            }
        }
        return notFoundResponseObject();
    }
    // 매장 추가
    @PostMapping(value = "/newstore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createStore(@ModelAttribute Store store,
                                              @RequestParam("imageFile") MultipartFile imageFile,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            try {
                // 이미지 저장 및 이미지 경로 설정
                Photo photo = imageService.saveImage(imageFile);
                store.setPhoto(photo);

                store.setAdmin(admin);
                admin.setStore(store);

                // 매장 등록 후 이미지 저장
                authService.saveUser(admin);

                return ResponseEntity.ok("매장 등록 성공");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("매장 이미지 업로드 실패");
            }
        }
        return notFoundResponse();
    }

    // 매장 수정 API
    @PutMapping(value = "/updatestore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateStore(@ModelAttribute Store updateStore,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        // 매장 조회
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin == null || admin.getStore() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("매장을 찾을 수 없음");
        }

        try {
            Store store = admin.getStore();

            // 이미지를 변경하고 이전 이미지를 삭제 예정으로 표시
            if (imageFile != null) {
                if (store.getPhoto() != null) {
                    Photo existingPhoto = store.getPhoto();
                    existingPhoto.setPhoto_status("1");
                }
                // 이미지 저장 및 매장에 연결
                Photo photo = imageService.saveImage(imageFile);
                store.setPhoto(photo);
            }

            // 매장 정보 업데이트 (입력된 값만 변경)
            if (updateStore.getStore_name() != null) {
                store.setStore_name(updateStore.getStore_name());
            }
            if (updateStore.getStore_address() != null) {
                store.setStore_address(updateStore.getStore_address());
            }
            if (updateStore.getStore_category() != null) {
                store.setStore_category(updateStore.getStore_category());
            }
            if (updateStore.getStore_phone() != null) {
                store.setStore_phone(updateStore.getStore_phone());
            }
            if (updateStore.getStore_intro() != null) {
                store.setStore_intro(updateStore.getStore_intro());
            }
            if (updateStore.getMinimumTime() != null) {
                store.setMinimumTime(updateStore.getMinimumTime());
            }
            if (updateStore.getRes_status() != null) {
                store.setRes_status(updateStore.getRes_status());
            }
            if (updateStore.getRes_max() != null) {
                store.setRes_max(updateStore.getRes_max());
            }
            if (updateStore.getOpenTime() != null) {
                store.setOpenTime(updateStore.getOpenTime());
            }
            if (updateStore.getCloseTime() != null) {
                store.setCloseTime(updateStore.getCloseTime());
            }
            if (updateStore.getReservationCancel() != null) {
                store.setReservationCancel(updateStore.getReservationCancel());
            }
            // 업데이트 및 저장
            authService.saveUser(admin);

            return ResponseEntity.ok("매장 정보 수정 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 오류");
        }
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

        // 매장 삭제
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
    //*********** 메뉴 엔드 포인트 ***********//
    // 메뉴 조회 API
    @GetMapping("/menus")
    public ResponseEntity<Object> getMenusByStore(@RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponseObject();

        // 매장 조회
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin != null && admin.getStore() != null) {
            // 매장 모든 메뉴 조회
            List<Menu> menus = admin.getStore().getMenus();

            List<MenuDTO> menuDTOs = new ArrayList<>();
            for (Menu menu : menus) {
                MenuDTO menuDTO = new MenuDTO(
                        menu.getMenu_id(),
                        menu.getMenu_price(),
                        menu.getMenu_name(),
                        menu.getExplanation(),
                        menu.getMenuPhoto().getImagePath()
                );
                menuDTOs.add(menuDTO);
            }
            return ResponseEntity.ok(menuDTOs);
        }
        return notFoundResponseObject();
    }
    // 메뉴 추가 API
    @PostMapping(value = "/newmenu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createMenu(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                             @ModelAttribute Menu newMenu,
                                             @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponse();

        // 메뉴 저장
        authService.saveMenu(newMenu, imageFile, admin_id);
        return ResponseEntity.ok("메뉴 등록 성공");
    }
    // 메뉴 수정 API
    @PutMapping(value = "/updatemenu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMenuInfo(@ModelAttribute Menu updateMenu,
                                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponse();

        // 관리자 ID 추출
        String adminId = jwtTokenProvider.extractUserId(token);
        if (adminId == null) return unauthorizedResponse();

        // 매장 조회
        Admin admin = (Admin) authService.getUser(adminId, UserType.ADMIN);
        if (admin == null || admin.getStore() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("매장을 찾을 수 없음");
        }

        try {
            Menu existingMenu = authService.findMenuById(updateMenu.getMenu_id());

            if (existingMenu != null) {
                // 이미지를 변경하고 기존 이미지를 삭제 예정으로 표시
                if (imageFile != null) {
                    if (existingMenu.getMenuPhoto() != null) {
                        Photo existingPhoto = existingMenu.getMenuPhoto();
                        existingPhoto.setPhoto_status("1");
                    }
                    Photo photo = imageService.saveImage(imageFile);
                    existingMenu.setMenuPhoto(photo);
                }

                // 메뉴 정보 업데이트 (입력된 값만 변경)
                if (updateMenu.getMenu_name() != null) {
                    existingMenu.setMenu_name(updateMenu.getMenu_name());
                }
                if (updateMenu.getMenu_price() != null) {
                    existingMenu.setMenu_price(updateMenu.getMenu_price());
                }
                if (updateMenu.getExplanation() != null){
                    existingMenu.setExplanation(updateMenu.getExplanation());
                }
                // 메뉴 정보 저장
                menuRepository.save(existingMenu);

                return ResponseEntity.ok("메뉴 수정 성공");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메뉴를 찾을 수 없음");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 오류");
        }
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

        // 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);

        Menu menu = authService.findMenuById(menu_id);
        if (menu == null || !menu.getStore().getAdmin().equals(admin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
        }

        // 메뉴 삭제
        try {
            authService.deleteMenu(menu_id);
            return ResponseEntity.ok("삭제 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제 실패");
        }
    }
    //************** 주문 *************//
    // 매장 예약 신청 내역 조회
    @GetMapping("/getReservation")
    public ResponseEntity<Object> getUserReservations(@RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponseObject();

        // 관리자가 소유한 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            Long store_id = admin.getStore().getStore_id();

            // 예약 내역 조회
            List<ReservationDTO> reservations = storeService.getReservations(store_id);
            return ResponseEntity.ok(reservations);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    // 매장 예약 상태 업데이트
    @PutMapping("/updateReservation")
    public ResponseEntity<Object> updateReservation(@RequestHeader("Authorization") String authorizationHeader , @RequestBody ReservationDTO reservationDTO) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponseObject();

        // 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            String reservationStatus = reservationDTO.getReservation_status();

            // 업데이트
            reservationDTO.setReservation_status(reservationStatus);
            storeService.updateReservation(reservationDTO);
            webSocketHandler.sendUpdateReservation(reservationDTO.getUser_id());
            return ResponseEntity.ok("포장 상태 업데이트");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    // 매장 포장 신청 내역 조회
    @GetMapping("/getPacking")
    public ResponseEntity<Object> getUserPackings(@RequestHeader("Authorization") String authorizationHeader) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponseObject();

        // 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            Long store_id = admin.getStore().getStore_id();

            // 포장 내역 조회
            List<PackingDTO> packings = storeService.getPacking(store_id);
            return ResponseEntity.ok(packings);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    // 매장 포장 상태 업데이트
    @PutMapping("/updatePacking")
    public ResponseEntity<Object> updatePacking(@RequestHeader("Authorization") String authorizationHeader , @RequestBody PackingDTO packingDTO) {
        // 인증 처리 및 토큰 추출
        String token = extractToken(authorizationHeader);
        if (token == null) return unauthorizedResponseObject();

        // 관리자 ID 추출
        String admin_id = jwtTokenProvider.extractUserId(token);
        if (admin_id == null) return unauthorizedResponseObject();

        // 매장 조회
        Admin admin = (Admin) authService.getUser(admin_id, UserType.ADMIN);
        if (admin != null) {
            String packingStatus = packingDTO.getPacking_status();

            // 업데이트
            packingDTO.setPacking_status(packingStatus);
            storeService.updatePacking(packingDTO);
            webSocketHandler.sendUpdatePacking(packingDTO.getUser_id());
            return ResponseEntity.ok("포장 상태 업데이트");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    //*************** 메서드 모음 ***************//
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