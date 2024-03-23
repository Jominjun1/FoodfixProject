package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class PackingStoreDTO {
    // 사용자측 정보
    private String user_id;
    private String user_phone;
    private String user_comments;
    private LocalDate packing_date;
    private LocalTime packing_time;

    // 매장 정보
    private String store_phone;
    private Long store_id;
    private Integer minimumTime;

    // 메뉴 정보
    private List<MenuItemDTO> menuItemDTOList;
    private Double totalPrice;

    // 포장 정보
    private Long packing_id;
    private String packing_status;

    public PackingStoreDTO(String user_id, String user_phone, String user_comments, LocalDate packing_date, LocalTime packing_time,
                           String store_phone, Long store_id, Integer minimumTime, List<MenuItemDTO> menuItemDTOList, Long packing_id, String packing_status) {
        this.user_id = user_id;
        this.user_phone = user_phone;
        this.user_comments = user_comments;
        this.packing_date = packing_date;
        this.packing_time = packing_time;
        this.store_phone = store_phone;
        this.store_id = store_id;
        this.minimumTime = minimumTime;
        this.menuItemDTOList = menuItemDTOList;
        this.packing_id = packing_id;
        this.packing_status = packing_status;
    }
}

