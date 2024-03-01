package com.project.foodfix.service;

import com.project.foodfix.UserType;
import com.project.foodfix.config.JwtTokenProvider;
import com.project.foodfix.model.*;
import com.project.foodfix.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }
    // 회원가입 기능
    public ResponseEntity<String> signup(Object user, UserType userType) {
        // 사용자 ID 중복 확인 등 회원가입 유효성 검사 수행
        if (isUserIdAvailable(user, userType)) {
            // 사용자 정보 저장
            saveUser(user);
            return ResponseEntity.ok("회원가입 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 ID 입니다.");
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "비밀번호가 일치하지 않습니다"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of());
        }
    }
    // 로그아웃 기능
    public void logout(String userId, UserType userType) {
        Optional<?> optionalUser = getUserById(userId, userType);

        optionalUser.ifPresent(user -> {
            if (user instanceof User) {
                ((User) user).setJwtToken(null);
            } else if (user instanceof Admin) {
                ((Admin) user).setJwtToken(null);
            }
            saveUser(user);
        });
    }
    // 정보 조회 기능
    public Object getUser(String userId, UserType userType) {
        Optional<?> optionalUser = getUserById(userId, userType);
        return optionalUser.orElse(null);
    }
    // 사용자 정보 저장 기능
    public void saveUser(Object user) {
        // 비밀번호 해싱
        hashUserPassword(user);
        // 저장
        if (user instanceof User) {
            userRepository.save((User) user);
        } else if (user instanceof Admin) {
            adminRepository.save((Admin) user);
        }
    }
    // 사용자 회원 탈퇴 기능
    public void deleteUser(String userId, UserType userType) {
        Optional<?> optionalUser = getUserById(userId, userType);
        if (optionalUser.isPresent()) {
            if (userType == UserType.USER) {
                userRepository.deleteById(userId);
            } else if (userType == UserType.ADMIN) {
                adminRepository.deleteById(userId);
            }
            ResponseEntity.ok("회원 탈퇴 성공");
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 정보를 찾을 수 없습니다.");
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
            case User u -> passwordEncoder.matches(password, u.getUser_pw());
            case Admin a -> passwordEncoder.matches(password, a.getAdmin_pw());
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
    // 비밀번호 해싱 메서드
    private void hashUserPassword(Object user) {
        if (user instanceof User u) {
            String hashedPassword = passwordEncoder.encode(u.getUser_pw());
            u.setUser_pw(hashedPassword);
        } else if (user instanceof Admin a) {
            String hashedPassword = passwordEncoder.encode(a.getAdmin_pw());
            a.setAdmin_pw(hashedPassword);
        }
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
}

