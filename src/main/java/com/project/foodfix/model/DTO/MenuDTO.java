package com.project.foodfix.model.DTO;

import lombok.*;

@Getter
@Setter
public class MenuDTO {
    private Long menu_id;
    private double menu_price;
    private String menu_name;
    private String explanation;
    private String imagePath;

    public MenuDTO(Long menu_id , double menu_price, String menu_name , String explanation , String imagePath){
        this.menu_id=menu_id;
        this.menu_name=menu_name;
        this.menu_price=menu_price;
        this.explanation=explanation;
        this.imagePath=imagePath;
    }
}
