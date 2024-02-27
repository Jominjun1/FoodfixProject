package com.project.foodfix.model.dto;

import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Store;
import com.project.foodfix.model.User;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class TakeoutDTO {
    private User user;
    private Store store;
    private List<OrderItemDTO> orderItems;
    private Integer  preparationTimeMinutes;
    private String requirements;
    private Integer  timeStatus;
    private Integer  paymentMethod;

    @Getter
    @Setter
    public static class OrderItemDTO {
        private Menu menu;
        private int quantity;
    }
}

