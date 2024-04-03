package com.project.foodfix.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class PackingDTO {
    // 사용자측 정보
    private String user_id;
    private String user_phone;
    private String user_comments;
    private LocalDate packing_date;
    private LocalTime packing_time;
    private String payment_type;

    // 매장 정보
    private String store_phone;
    private Long store_id;
    private Integer minimumTime;

    // 메뉴 정보
    private List<MenuItemDTO> menuItemDTOList;

    // 포장 정보
    private Long packing_id;
    private String packing_status;

}

