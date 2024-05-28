package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ReservableStoreDTO {
    private Long store_id;
    private String store_address;
    private String store_phone;
    private String store_intro;
    private String imagePath;
    private String store_name;
    private String store_category;
    private Integer res_max;
    private LocalTime openTime;
    private LocalTime closeTime;

    public ReservableStoreDTO(Long store_id , String store_address, String store_phone , String store_intro , Integer res_max,
                              String imagePath, String store_name, String store_category,LocalTime openTime, LocalTime closeTime) {
        this.store_id=store_id;
        this.store_address=store_address;
        this.store_phone=store_phone;
        this.store_intro=store_intro;
        this.res_max=res_max;
        this.imagePath = imagePath;
        this.store_name = store_name;
        this.store_category = store_category;
        this.openTime=openTime;
        this.closeTime=closeTime;
    }
}