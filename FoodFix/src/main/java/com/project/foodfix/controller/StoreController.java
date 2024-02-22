package com.project.foodfix.controller;

import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Store;
import com.project.foodfix.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }
    // 해당 매장의 메뉴 조회 API
    @GetMapping("/{store_id}/menus")
    public List<Menu> getAllMenusForStore(@PathVariable("store_id") Long store_id) {
        return storeService.getAllMenusForStore(store_id);
    }
    // 매장 정보 수정 API
    @PutMapping("/update/{store_id}")
    public void updateStore(@PathVariable("store_id") Long store_id, @RequestBody Store updatedStore) {
        storeService.updateStore(store_id, updatedStore);
    }
    // 매장 메뉴 삽입 API
    @PostMapping("/{admin_id}/store")
    public ResponseEntity<String> addStore(@PathVariable("admin_id") String admin_id, @RequestBody Store store) {
        return storeService.addStore(admin_id, store);
    }
    // 매장 삭제 API
    @DeleteMapping("/delete/{store_id}")
    public void deleteStore(@PathVariable("store_id") Long store_id) {
        storeService.deleteStore(store_id);
    }

}