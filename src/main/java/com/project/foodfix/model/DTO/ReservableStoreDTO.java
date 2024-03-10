package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ReservableStoreDTO {
    private String store_name;
    private String store_image;
    private String store_category;
    private LocalTime openTime;
    private LocalTime closeTime;

    public ReservableStoreDTO(String store_name, String store_image, String store_category , LocalTime openTime , LocalTime closeTime) {
        this.store_name = store_name;
        this.store_image = store_image;
        this.store_category = store_category;
        this.openTime=openTime;
        this.closeTime=closeTime;
    }
}