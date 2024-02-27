package com.project.foodfix.controller;

import com.project.foodfix.model.Reservation;
import com.project.foodfix.model.TakeoutOrder;
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
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService; // 변경된 부분
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 해당 사용자의 포장 주문 조회 API
    @GetMapping("/orderList/{user_id}")
    public List<TakeoutOrder> getOrderHistory(@PathVariable("user_id") String user_id) {
        return userService.getOrderHistory(user_id);
    }
    // 해당 사용자의 예약 주문 조회 API
    @GetMapping("/reservationList/{user_id}")
    public List<Reservation> getReservationHistory(@PathVariable("user_id") String user_id) {
        return userService.getReservationHistory(user_id);
    }
    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO loginRequest) {
        return userService.login(loginRequest);
    }
    // 사용자 정보 수정 API
    @PutMapping("/info")
    public void putUser(@RequestBody User updatedUser) {
        String user_id = updatedUser.getUser_id();
        userService.putUser(user_id, updatedUser);
    }
    // 사용자 등록 API
    @PostMapping("/signup")
    public void postUser(@RequestBody User user) {
        String user_id = user.getUser_id();
        userService.postUser(user_id, user);
    }
    // 사용자 삭제 API
    @DeleteMapping("/del")
    public void deleteUser(@RequestBody UserDTO userDTO) {
        String user_id = userDTO.getUser_id();
        userService.deleteUser(user_id);
    }
}
