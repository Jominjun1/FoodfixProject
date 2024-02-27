package com.project.foodfix.controller;

import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Store;
import com.project.foodfix.service.StoreService;
import jakarta.servlet.http.HttpSession;
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
    // 해당 매장의 메뉴 리스트
    @GetMapping("/{store_id}/menus")
    public List<Menu> getAllMenusForStore(@PathVariable("store_id") Long store_id) {
        return storeService.getAllMenusForStore(store_id);
    }
    // 매장 수정
    @PutMapping("/update/{store_id}")
    public void updateStore(@PathVariable("store_id") Long store_id,  @RequestBody Store updatedStore, HttpSession session) {
        String admin_id = (String) session.getAttribute("login_admin");
        storeService.updateStore(admin_id, store_id, updatedStore);
    }
    // 매장 추가
    @PostMapping("/create_store")
    public ResponseEntity<String> addStore(@RequestBody Store store, HttpSession session) {
        String admin_id = (String) session.getAttribute("login_admin");
        storeService.addStore(admin_id, store);
        return ResponseEntity.ok("매장이 추가 되었습니다.");
    }
    // 매장 삭제
    @DeleteMapping("/delete/{store_id}")
    public void deleteStore(@PathVariable("store_id") Long store_id, HttpSession session) {
        String admin_id = (String) session.getAttribute("login_admin");
        storeService.deleteStore(admin_id, store_id);
    }
}
