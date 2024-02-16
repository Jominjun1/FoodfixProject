package com.project.foodfix.controller;

import com.project.foodfix.model.Admin;
import com.project.foodfix.model.dto.AdminDTO;
import com.project.foodfix.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping("/info/{phone}")
    public Admin getAdmin(@PathVariable("phone") String phone) {
        return adminRepository.findById(phone).orElse(null);
    }

    @GetMapping("/all")
    public List<AdminDTO> getAdmins() {
        List<Admin> admins = adminRepository.findAll();
        List<AdminDTO> adminDTOs = new ArrayList<>();

        for (Admin admin : admins) {
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(admin.getId());
            adminDTO.setPw(admin.getPw());
            adminDTOs.add(adminDTO);
        }

        return adminDTOs;
    }


    @PutMapping("/info_update/{phone}")
    public void putAdmin(@PathVariable("phone") String phone, @RequestBody Admin updatedAdmin) {
        Admin existingAdmin = adminRepository.findById(phone).orElse(null);
        if (existingAdmin != null) {
            // Update the existing Admin
            existingAdmin.setPw(updatedAdmin.getPw());
            existingAdmin.setPhone(updatedAdmin.getPhone());
            existingAdmin.setName(updatedAdmin.getName());
            existingAdmin.setAddress(updatedAdmin.getAddress());
            adminRepository.save(existingAdmin);
        }
    }

    @PostMapping("/signup/{phone}")
    public void postAdmin(@PathVariable("phone") String phone, @RequestBody Admin admin) {
        adminRepository.save(admin);
    }

    @DeleteMapping("/delAdmin/{phone}")
    public void deleteAdmin(@PathVariable("phone") String phone) {
        adminRepository.deleteById(phone);
    }
}
