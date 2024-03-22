package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ReservableStoreDTO {
    private Long store_id;
    private String store_name;
    private String imagePath;
    private String store_category;
    private LocalTime openTime;
    private LocalTime closeTime;

    public ReservableStoreDTO(Long store_id , String imagePath, String store_name, String store_category,LocalTime openTime, LocalTime closeTime) {
        this.store_id=store_id;
        this.imagePath = imagePath;
        this.store_name = store_name;
        this.store_category = store_category;
        this.openTime=openTime;
        this.closeTime=closeTime;
    }
}