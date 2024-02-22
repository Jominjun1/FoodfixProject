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
    private String admin_id;

    private String admin_address;
    private String admin_name;
    private String admin_phone;
    private String admin_pw;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Store> stores = new ArrayList<>();
}
