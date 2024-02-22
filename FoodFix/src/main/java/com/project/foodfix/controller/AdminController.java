package com.project.foodfix.controller;

import com.project.foodfix.model.Admin;
import com.project.foodfix.model.Store;
import com.project.foodfix.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final AdminService adminService;
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    // 관리자 등록 API
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Admin admin) {
        return adminService.signup(admin);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Admin loginRequest) {
        return adminService.login(loginRequest);
    }

    // 모든 관리자 정보 조회 API
    @GetMapping("/all")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // 특정 관리자 정보 조회 API
    @GetMapping("/{admin_id}")
    public Admin getAdmin(@PathVariable("admin_id") String admin_id) {
        return adminService.getAdmin(admin_id);
    }

    // 해당 관리자 ID의 매장 조회 API
    @GetMapping("/{admin_id}/stores")
    public List<Store> getAllStoresForAdmin(@PathVariable("admin_id") String admin_id) {
        return adminService.getAllStoresForAdmin(admin_id);
    }

    // 관리자 정보 수정 API
    @PutMapping("/update/{admin_id}")
    public void updateAdmin(@PathVariable("admin_id") String admin_id, @RequestBody Admin updatedAdmin) {
        adminService.updateAdmin(admin_id, updatedAdmin);
    }

    // 관리자 삭제 API
    @DeleteMapping("/delete/{admin_id}")
    public void deleteAdmin(@PathVariable("admin_id") String admin_id) {
        adminService.deleteAdmin(admin_id);
    }
}
