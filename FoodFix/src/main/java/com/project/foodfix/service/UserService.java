package com.project.foodfix.service;

import com.project.foodfix.model.User;
import com.project.foodfix.model.dto.UserDTO;
import com.project.foodfix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // 특정 사용자 정보 조회 메서드
    public User getUser(String user_id) {
        return userRepository.findById(user_id).orElse(null);
    }
    // 모든 사용자 정보 조회 메서드
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
    // 로그인 메서드
    public ResponseEntity<String> login(UserDTO loginRequest) {
        String user_id = loginRequest.getUser_id();
        String user_pw = loginRequest.getUser_pw();

        Optional<User> optionalUser = userRepository.findById(user_id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getUser_pw().equals(user_pw)) {
                return ResponseEntity.ok("로그인 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다");
        }
    }
    // 사용자 정보 수정 메서드
    public void putUser(String user_id, User updatedUser) {
        User existingUser = userRepository.findById(user_id).orElse(null);
        if (existingUser != null) {
            existingUser.setUser_pw(updatedUser.getUser_pw());
            existingUser.setUser_address(updatedUser.getUser_address());
            existingUser.setNickname(updatedUser.getNickname());
            userRepository.save(existingUser);
        }
    }
    // 사용자 등록 메서드
    public void postUser(String user_id, User user) {
        userRepository.save(user);
    }
    // 사용자 삭제 메서드
    public void deleteUser(String user_id) {
        userRepository.deleteById(user_id);
    }
}
