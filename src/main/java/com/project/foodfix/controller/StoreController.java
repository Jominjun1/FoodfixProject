package com.project.foodfix.controller;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;
import com.project.foodfix.service.Store.StoreService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // 포장 검색
    @GetMapping("/packable")
    public ResponseEntity<List<PackableStoreDTO>> searchPackableStores(
            @RequestParam(required = false) String store_category,
            @RequestParam(required = false) String store_name,
            @RequestParam(required = false) String menu_name) {
        //  검색 결과 반환
        List<PackableStoreDTO> result = storeService.searchPackableStores(store_category, store_name, menu_name);
        return ResponseEntity.ok(result);
    }
    // 예약 검색
    @GetMapping("/reservable")
    public ResponseEntity<List<ReservableStoreDTO>> searchReservableStores(
            @RequestParam(required = false) String store_category,
            @RequestParam(required = false) String store_name,
            @RequestParam(required = false) String menu_name) {
        //검색 결과 반환
        List<ReservableStoreDTO> result = storeService.searchReservableStores(store_category, store_name, menu_name);
        return ResponseEntity.ok(result);
    }

}