package com.project.foodfix.controller;

import com.project.foodfix.model.User;
import com.project.foodfix.model.dto.UserDTO;
import com.project.foodfix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{user_phone}")
    public User getUser(@PathVariable("user_phone") String user_phone) {
        return userRepository.findById(user_phone).orElse(null);
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

    @PutMapping("/info/{user_phone}")
    public void putUser(@PathVariable("user_phone") String user_phone, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(user_phone).orElse(null);
        if (existingUser != null) {
            // 유저 테이블 수정
            existingUser.setUser_pw(updatedUser.getUser_pw());
            existingUser.setUser_phone(updatedUser.getUser_phone());
            existingUser.setUser_name(updatedUser.getUser_name());
            existingUser.setUser_address(updatedUser.getUser_address());
            existingUser.setNickname(updatedUser.getNickname());
            existingUser.setMale(updatedUser.getMale());
            userRepository.save(existingUser);
        }
    }

    @PostMapping("/signup/{user_phone}")
    public void postUser(@PathVariable("user_phone") String user_phone, @RequestBody User user) {
        userRepository.save(user);
    }
    @DeleteMapping("/del/{user_phone}")
    public void deleteUser(@PathVariable("user_phone") String user_phone) {
        userRepository.deleteById(user_phone);
    }
}
