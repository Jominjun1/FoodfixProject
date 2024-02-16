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
public class User {

    @Id
    private String user_phone; // primary key + unique key

    private String user_id; // unique key
    private String user_name; // unique key
    private String user_pw; // unique key
    private String user_address; // unique key
    private String nickname;
    private String male;

    public User() {
    }
}
