package com.project.foodfix.controller;

import com.project.foodfix.model.Store;
import com.project.foodfix.service.StoreService;
import org.springframework.http.HttpStatus;
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
    // 로그인한 관리자에 해당되는 매장 조회
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<Object> getAdminStores(@PathVariable String adminId) {
        List<Store> stores = storeService.getStoresByAdminId(adminId);
        if (!stores.isEmpty()) {
            return ResponseEntity.ok(stores);
        }
        return notFoundResponseObject();
    }
    // 로그인한 관리자의 매장 생성
    @PostMapping("/admin/{adminId}")
    public ResponseEntity<String> createAdminStore(@PathVariable String adminId, @RequestBody Store newStore) {
        String result = storeService.createStoreForAdmin(adminId, newStore);
        if (result.equals("성공")) {
            return ResponseEntity.ok("매장 생성 완료");
        }
        return notFoundResponse();
    }
    // 로그인한 관리자의 매장 수정
    @PutMapping("/admin/{adminId}/{storeId}")
    public ResponseEntity<String> updateAdminStore(@PathVariable String adminId, @PathVariable Long storeId, @RequestBody Store updatedStore) {
        String result = storeService.updateStoreForAdmin(adminId, storeId, updatedStore);
        if (result.equals("성공")) {
            return ResponseEntity.ok("매장 수정 완료");
        }
        return notFoundResponse();
    }
    // 로그인한 관리자의 매장 삭제
    @DeleteMapping("/admin/{adminId}/{storeId}")
    public ResponseEntity<String> deleteAdminStore(@PathVariable String adminId, @PathVariable Long storeId) {
        String result = storeService.deleteStoreForAdmin(adminId, storeId);
        if (result.equals("성공")) {
            return ResponseEntity.ok("매장 삭제 완료");
        }
        return notFoundResponse();
    }
    // 찾을 수 없음 응답 ( Object )
    private ResponseEntity<Object> notFoundResponseObject() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("관리자를 찾을 수 없습니다.");
    }
    // 찾을 수 없음 응답
    private ResponseEntity<String> notFoundResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("관리자를 찾을 수 없습니다.");
    }
}
