package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class PackableStoreDTO {
    private String store_image;
    private String store_name;
    private String store_category;
    private Integer minimumTime;
    private LocalTime openTime;
    private LocalTime closeTime;

    public PackableStoreDTO(String store_image, String store_name, String store_category, Integer minimumTime ,LocalTime openTime, LocalTime closeTime) {
        this.store_image = store_image;
        this.store_name = store_name;
        this.store_category = store_category;
        this.minimumTime = minimumTime;
        this.openTime=openTime;
        this.closeTime=closeTime;
    }
}