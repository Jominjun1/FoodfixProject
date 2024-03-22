package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class PackableStoreDTO {
    private String imagePath;
    private String store_name;
    private String store_category;
    private Integer minimumTime;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long store_id; // 매장 ID

    public PackableStoreDTO(Long store_id , String imagePath, String store_name, String store_category, Integer minimumTime ,LocalTime openTime, LocalTime closeTime) {
        this.store_id=store_id;
        this.imagePath = imagePath;
        this.store_name = store_name;
        this.store_category = store_category;
        this.minimumTime = minimumTime;
        this.openTime=openTime;
        this.closeTime=closeTime;
    }
}