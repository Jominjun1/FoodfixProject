// Admin.java
package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL) // (관리자)1대 다(매장) 관계
    private List<Store> stores = new ArrayList<>();
}
