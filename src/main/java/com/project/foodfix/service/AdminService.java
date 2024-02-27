package com.project.foodfix.service;

import com.project.foodfix.model.Admin;
import com.project.foodfix.model.Store;
import com.project.foodfix.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    // 관리자 등록 메서드
    public ResponseEntity<String> signup(Admin admin) {
        if (adminRepository.existsById(admin.getAdmin_id())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }
        adminRepository.save(admin);
        return ResponseEntity.ok("회원 가입 성공");
    }
    // 로그인 메서드
    public ResponseEntity<String> login(Admin loginRequest) {
        String admin_id = loginRequest.getAdmin_id();
        String admin_pw = loginRequest.getAdmin_pw();

        Optional<Admin> optionalAdmin = adminRepository.findById(admin_id);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            if (admin.getAdmin_pw().equals(admin_pw)) {
                return ResponseEntity.ok("로그인 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자를 찾을 수 없습니다");
        }
    }
    // 모든 관리자 정보 조회 메서드
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    // 특정 관리자 정보 조회 메서드
    public Admin getAdmin(String admin_id) {
        return adminRepository.findById(admin_id).orElse(null);
    }
    // 관리자 정보 수정 메서드
    public void updateAdmin(String admin_id, Admin updatedAdmin) {
        Admin existingAdmin = adminRepository.findById(admin_id).orElse(null);
        if (existingAdmin != null) {
            existingAdmin.setAdmin_pw(updatedAdmin.getAdmin_pw());
            existingAdmin.setAdmin_address(updatedAdmin.getAdmin_address());
            existingAdmin.setAdmin_name(updatedAdmin.getAdmin_name());
            adminRepository.save(existingAdmin);
        }
    }
    // 해당 관리자 매장 정보 가져옴
    public List<Store> getAllStoresForAdmin(String admin_id) {
        Admin admin = adminRepository.findById(admin_id).orElse(null);

        if (admin != null) {
            return admin.getStores();
        } else {
            return Collections.emptyList();
        }
    }
    // 관리자 삭제 메서드
    public void deleteAdmin(String admin_id) {
        adminRepository.deleteById(admin_id);
    }
}