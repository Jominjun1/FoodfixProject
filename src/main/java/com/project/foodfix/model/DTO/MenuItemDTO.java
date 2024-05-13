package com.project.foodfix.model.DTO;

import com.project.foodfix.model.Packing;
import lombok.*;

@Getter
@Setter
public class MenuItemDTO {
    private Long menu_id;
    private double menu_price;
    private double totalPrice;
    private String menu_name;
    private int quantity;

    public MenuItemDTO(Long menuitem_id, double menu_price,double totalPrice, String menu_name, int quantity, Packing packing) {
        this.menu_id = menuitem_id;
        this.menu_price = menu_price;
        this.totalPrice = totalPrice;
        this.menu_name = menu_name;
        this.quantity = quantity;
    }

    public MenuItemDTO() {

    }
}
