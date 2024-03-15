package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    private String user_id; // 사용자 ID + 기본키

    private String user_phone; // 사용자 핸드폰 + 유니크 키
    private String user_name; // 사용자 이름
    private String user_pw; // 사용자 비밀번호
    private String user_address; // 사용자 주소
    private String nickname; // 사용자 닉네임
    private String gender; // 사용자 성별 ( 0 이면 남자 , 1 이면 여자 )

    @Column(name = "jwt_token")
    private String jwtToken; // 로그인 할 때 받는 토큰 저장

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}