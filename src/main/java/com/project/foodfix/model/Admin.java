package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @Column(name = "admin_id")
    private String admin_id; // 관리자 아이디 + 유니크 키

    private String admin_address; // 관리자 주소
    private String admin_name; // 관리자 이름
    private String admin_phone; // 관리자 전화번호
    private String admin_pw; // 관리자 비밀번호

    @Column(name = "jwt_token")
    private String jwtToken; // 로그인 할때 받는 토큰 저장

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL , fetch = FetchType.EAGER) // (관리자) 일대다 (매장) 관계
    private List<Store> stores = new ArrayList<>();

}
