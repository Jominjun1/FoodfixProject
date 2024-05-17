package com.project.foodfix.service;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.model.*;
import com.project.foodfix.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final PhotoRepository photoRepository;
    private final ReservationRepository reservationRepository;
    private final PackingRepository packingRepository;
    private final ImageService imageService;

    @Autowired
    public AuthService(UserRepository userRepository, AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider, StoreRepository storeRepository, MenuRepository menuRepository, PhotoRepository photoRepository, ReservationRepository reservationRepository, PackingRepository packingRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.photoRepository = photoRepository;
        this.reservationRepository = reservationRepository;
        this.packingRepository = packingRepository;
        this.imageService = imageService;
    }
    // 회원가입 기능
    public ResponseEntity<String> signup(Object user, UserType userType) {
        // 사용자 ID 중복 확인 등 회원가입 유효성 검사 수행
        if (isUserIdAvailable(user, userType)) {
            // 사용자 정보 저장
            saveUser(user);
            return ResponseEntity.ok("회원가입 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 ID");
        }
    }
    // 로그인 기능
    public ResponseEntity<Map<String, String>> login(String id, String password, UserType userType) {
        Optional<?> optionalUser = getUserById(id, userType);
        if (optionalUser.isPresent()) {
            Object user = optionalUser.get();

            if (isValidPassword(user, password)) {
                String jwtToken = jwtTokenProvider.createToken(id);
                setTokenForUser(user, jwtToken);
                saveUser(user);

                Map<String, String> response = new HashMap<>();
                response.put("message", "로그인 성공");
                response.put(getTokenKey(userType), id);
                response.put("token", jwtToken);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "비밀번호가 일치하지 않음"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "사용자를 찾을 수 없음"));
        }
    }
    // 로그아웃 기능
    public void logout(String user_id, UserType userType) {
        Optional<?> optionalUser = getUserById(user_id, userType);

        optionalUser.ifPresent(user -> {
            if (user instanceof User) {
                ((User) user).setJwtToken(null);
                saveUser(user);
            } else if (user instanceof Admin) {
                ((Admin) user).setJwtToken(null);
                saveUser(user);
            }
        });
    }
    // 메뉴 추가
    public void saveMenu(Menu newMenu, MultipartFile imageFile, String adminId) {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            Store store = admin.getStore();

            if (store != null) {
                newMenu.setStore(store);
                try {
                    // 이미지 저장
                    Photo photo;
                    if (imageFile != null && !imageFile.isEmpty()) {
                        photo = imageService.saveImage(imageFile);
                        newMenu.setMenuPhoto(photo);
                    }
                } catch (IOException e) {
                    // 오류 발생 시 처리
                    ResponseEntity.ok("이미지 저장 오류");
                }
                store.getMenus().add(newMenu);
                saveUser(admin);
                ResponseEntity.ok("메뉴 추가 성공");
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("관리자가 소유한 매장 없음");
            }
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("관리자를 찾을 수 없음");
        }
    }
    // 사용자 회원 탈퇴 기능
    public void deleteUser(String user_id, UserType userType) {
        Optional<?> optionalUser = getUserById(user_id, userType);
        if (optionalUser.isPresent()) {
            if (userType == UserType.USER) {
                Optional<User> userdel = userRepository.findById(user_id);
                if (userdel.isPresent()) {
                    deleteOrder(user_id);
                    userRepository.deleteById(user_id);
                }
            } else if (userType == UserType.ADMIN) {
                Optional<Admin> adminDel = adminRepository.findById(user_id);
                if (adminDel.isPresent()) {
                    Admin admin = adminDel.get();
                    Store store = admin.getStore();
                    if (store != null) {
                        deleteStore(store.getStore_id());
                        adminRepository.deleteByAdmin(admin.getAdmin_id());
                    }
                }
                ResponseEntity.ok("회원 탈퇴 성공");
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("정보를 찾을 수 없음");
            }
        }
    }
    // 매장 삭제
    public void deleteStore(Long store_id) {
        try {
            // 매장과 관련된 사진 삭제
            Optional<Store> storeOptional = storeRepository.findById(store_id);
            if (storeOptional.isPresent()) {
                Store store = storeOptional.get();
                if (store.getPhoto() != null) {
                    Photo photo = store.getPhoto();
                    photo.setPhoto_status("1"); // 삭제 예정으로 상태 변경
                    photoRepository.save(photo);
                }
            }
            packingRepository.deleteMenuItemsByStoreId(store_id);
            packingRepository.deleteByStoreId(store_id);
            reservationRepository.deleteByStoreId(store_id);
            // 매장과 연관된 메뉴들 삭제
            menuRepository.deleteMenusByStoreId(store_id);
            // 매장 삭제
            storeRepository.deleteById(store_id);

        } catch (Exception e) {
            // 오류 메시지 출력
            System.err.println("오류 발생: " + e.getMessage());
        }
    }
    // 메뉴 삭제
    public void deleteMenu(Long menu_id) {
        try {
            Menu menu = menuRepository.findById(menu_id).orElse(null);
            if (menu != null && menu.getMenuPhoto() != null) {
                Photo photo = menu.getMenuPhoto();
                photo.setPhoto_status("1"); // 삭제 예정으로 상태 변경
                photoRepository.save(photo);
            }
            // 메뉴 삭제
            menuRepository.deleteByMenuId(menu_id);
        } catch (Exception e) {
            // 콘솔 오류 메시지 출력
            System.err.println("오류 발생: " + e.getMessage());
        }
    }
    // 주문 내역 삭제
    public void deleteOrder(String user_id){
        try{
            packingRepository.deleteMenuItemsByUserid(user_id);
            packingRepository.deleteByUserId(user_id);
            reservationRepository.deleteByUserId(user_id);
        } catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
        }
    }
    // 사용자 또는 관리자 ID에 따라 사용자 정보 조회
    private Optional<?> getUserById(String id, UserType userType) {
        return switch (userType) {
            case USER -> userRepository.findById(id);
            case ADMIN -> adminRepository.findById(id);
        };
    }
    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(Object user, String password) {
        return switch (user) {
            case User u -> u.getUser_pw().equals(password);
            case Admin a -> a.getAdmin_pw().equals(password);
            default -> false;
        };
    }
    // 사용자 또는 관리자에게 토큰 설정
    private void setTokenForUser(Object user, String jwtToken) {
        if (user instanceof User) {
            ((User) user).setJwtToken(jwtToken);
        } else if (user instanceof Admin) {
            ((Admin) user).setJwtToken(jwtToken);
        }
    }
    // UserType 따라 토큰의 키 반환
    private String getTokenKey(UserType userType) {
        return switch (userType) {
            case USER -> "user_id";
            case ADMIN -> "admin_id";
        };
    }
    // 사용자 ID 중복 확인 메서드
    private boolean isUserIdAvailable(Object user, UserType userType) {
        switch (userType) {
            case USER:
                if (user instanceof User) {
                    return !userRepository.existsById(((User) user).getUser_id());
                } else {
                    throw new IllegalArgumentException("잘못된 사용자 유형");
                }
            case ADMIN:
                if (user instanceof Admin) {
                    return !adminRepository.existsById(((Admin) user).getAdmin_id());
                } else {
                    throw new IllegalArgumentException("잘못된 사용자 유형");
                }
            default:
                throw new IllegalArgumentException("잘못된 사용자 유형");
        }
    }
    // 메뉴 찾기 기능
    public Menu findMenuById(Long menu_id) {
        Optional<Menu> optionalMenu = menuRepository.findById(menu_id);
        return optionalMenu.orElse(null);
    }
    // 정보 조회 기능
    public Object getUser(String user_id, UserType userType) {
        Optional<?> optionalUser = getUserById(user_id, userType);
        return optionalUser.orElse(null);
    }
    // 사용자 정보 저장 기능
    public void saveUser(Object user) {
        // 저장
        if (user instanceof User) {
            userRepository.save((User) user);
        } else {
            adminRepository.save((Admin) user);
        }
    }
}

