package com.project.foodfix.controller;

import com.project.foodfix.model.User;
import com.project.foodfix.model.dto.UserDTO;
import com.project.foodfix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{user_id}")
    public User getUser(@PathVariable("user_id") String user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    @GetMapping("/all")
    public List<UserDTO> getUserList() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUser_id(user.getUser_id());
            userDTO.setUser_pw(user.getUser_pw());
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO loginRequest) {
        String user_id = loginRequest.getUser_id();
        String user_pw = loginRequest.getUser_pw();

        // Retrieve user by ID
        Optional<User> optionalUser = userRepository.findById(user_id);

        if (optionalUser.isPresent()) {
            // User found, compare passwords
            User user = optionalUser.get();
            if (user.getUser_pw().equals(user_pw)) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }

    @PutMapping("/info/{user_id}")
    public void putUser(@PathVariable("user_id") String user_id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(user_id).orElse(null);
        if (existingUser != null) {
            // 유저 테이블 수정
            existingUser.setUser_pw(updatedUser.getUser_pw());
            existingUser.setUser_address(updatedUser.getUser_address());
            existingUser.setNickname(updatedUser.getNickname());
            userRepository.save(existingUser);
        }
    }

    @PostMapping("/signup/{user_id}")
    public void postUser(@PathVariable("user_id") String user_id, @RequestBody User user) {
        userRepository.save(user);
    }
    @DeleteMapping("/del/{user_id}")
    public void deleteUser(@PathVariable("user_id") String user_id) {
        userRepository.deleteById(user_id);
    }
}
