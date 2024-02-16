package com.project.foodfix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminDTO {
    private String id;
    private String pw;

    public AdminDTO(){

    }
}
