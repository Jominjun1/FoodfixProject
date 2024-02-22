package com.project.foodfix.controller;

import com.project.foodfix.model.User;
import com.project.foodfix.model.dto.UserDTO;
import com.project.foodfix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService; // 변경된 부분
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 특정 사용자 정보 조회 API
    @GetMapping("/{user_id}")
    public User getUser(@PathVariable("user_id") String user_id) {
        return userService.getUser(user_id);
    }
    // 모든 사용자 정보 조회 API
    @GetMapping("/all")
    public List<UserDTO> getUserList() {
        // 모든 사용자 정보를 조회하고 DTO로 변환하여 반환
        return userService.getUserList();
    }
    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO loginRequest) {
        return userService.login(loginRequest);
    }
    // 사용자 정보 수정 API
    @PutMapping("/info/{user_id}")
    public void putUser(@PathVariable("user_id") String user_id, @RequestBody User updatedUser) {
        userService.putUser(user_id, updatedUser);
    }
    // 사용자 등록 API
    @PostMapping("/signup/{user_id}")
    public void postUser(@PathVariable("user_id") String user_id, @RequestBody User user) {
        userService.postUser(user_id, user);
    }
    // 사용자 삭제 API
    @DeleteMapping("/del/{user_id}")
    public void deleteUser(@PathVariable("user_id") String user_id) {
        userService.deleteUser(user_id);
    }
}
