package com.project.foodfix.controller;

import com.project.foodfix.model.Admin;
import com.project.foodfix.model.Store;
import com.project.foodfix.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Controller
@CrossOrigin(origins = "http://localhost:3000")
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
    @GetMapping("/profile")
    public ResponseEntity<Admin> getAdminProfile(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("login_admin");
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }
    // 관리자 정보 수정 API
    @PutMapping("/update")
    public void updateAdmin(@RequestBody Admin updatedAdmin, HttpSession session) {
        Admin loggedAdmin = (Admin) session.getAttribute("login_admin");
        if (loggedAdmin != null) {
            adminService.updateAdmin(loggedAdmin.getAdmin_id(), updatedAdmin);
            session.setAttribute("login_admin", updatedAdmin);
        }
    }
    // 해당 관리자의 매장 확인
    @GetMapping("/stores")
    public List<Store> getAllStoresForAdmin(HttpSession session) {
        Admin loggedAdmin = (Admin) session.getAttribute("login_admin");
        if (loggedAdmin != null) {
            return adminService.getAllStoresForAdmin(loggedAdmin.getAdmin_id());
        } else {
            return Collections.emptyList();
        }
    }
    // 관리자 삭제 API
    @DeleteMapping("/delete")
    public void deleteAdmin(HttpSession session) {
        Admin loggedAdmin = (Admin) session.getAttribute("login_admin");
        if (loggedAdmin != null) {
            adminService.deleteAdmin(loggedAdmin.getAdmin_id());
            session.invalidate();
        }
    }
}
