package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class PackableStoreDTO {
    private Long store_id;
    private String store_phone;
    private String store_address;
    private String store_intro;
    private String imagePath;
    private String store_name;
    private String store_category;
    private Integer minimumTime;
    private LocalTime openTime;
    private LocalTime closeTime;

    public PackableStoreDTO(Long store_id , String store_phone,String store_address , String store_intro ,String imagePath,
                            String store_name, String store_category, Integer minimumTime ,LocalTime openTime, LocalTime closeTime) {
        this.store_id=store_id;
        this.store_phone=store_phone;
        this.store_address=store_address;
        this.store_intro=store_intro;
        this.imagePath = imagePath;
        this.store_name = store_name;
        this.store_category = store_category;
        this.minimumTime = minimumTime;
        this.openTime=openTime;
        this.closeTime=closeTime;
    }
}