package com.project.foodfix.service;

import com.project.foodfix.model.Reservation;
import com.project.foodfix.model.TakeoutOrder;
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
    // 로그인 메서드
    public ResponseEntity<String> login(UserDTO userdto) {
        String user_id = userdto.getUser_id();
        String user_pw = userdto.getUser_pw();

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
        User putUser = userRepository.findById(user_id).orElse(null);
        if (putUser != null) {
            if(putUser.getUser_pw() != null) {
                putUser.setUser_pw(updatedUser.getUser_pw());
            }
            if(putUser.getUser_address() != null) {
                putUser.setUser_address(updatedUser.getUser_address());
            }
            if(putUser.getNickname() != null){
                putUser.setNickname(updatedUser.getNickname());
            }
            userRepository.save(putUser);
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
    // 사용자가 포장 주문한 리스트 확인 메서드
    public List<TakeoutOrder> getOrderHistory(String user_id) {
        User user = userRepository.findById(user_id).orElse(null);
        if (user != null) {
            return user.getTakeoutOrders();
        } else {
            return Collections.emptyList();
        }
    }
    // 사용자가 예약한 리스트 확인 메서드
    public List<Reservation> getReservationHistory(String user_id) {
        User user = userRepository.findById(user_id).orElse(null);
        if (user != null) {
            return user.getReservations();
        } else {
            return Collections.emptyList();
        }
    }

}