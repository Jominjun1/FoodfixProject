package com.project.foodfix.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Admin {

    @Id
    private String phone; // primary key

    private String id; // unique key
    private String name; // unique key
    private String pw; // unique key
    private String address; // unique key

    public Admin() {
    }
}
