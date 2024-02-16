package com.project.foodfix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String user_id;
    private String user_pw;

    public UserDTO(){

    }
}
