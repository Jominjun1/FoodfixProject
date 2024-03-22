package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "admin")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "admin_id")
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

    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, unique = true)
    private Store store;

}
