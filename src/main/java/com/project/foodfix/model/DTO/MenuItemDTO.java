package com.project.foodfix.model.DTO;

import lombok.*;

@Getter
@Setter
public class MenuItemDTO {
    private Long menu_id;
    private double menu_price;
    private String menu_name;

    public MenuItemDTO(Long menu_id , double menu_price , String menu_name){
        this.menu_id=menu_id;
        this.menu_name=menu_name;
        this.menu_price=menu_price;
    }
}
