package com.project.foodfix.service;

import com.project.foodfix.model.Admin;
import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Store;
import com.project.foodfix.repository.AdminRepository;
import com.project.foodfix.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;
    @Autowired
    public StoreService(StoreRepository storeRepository, AdminRepository adminRepository) {
        this.storeRepository = storeRepository;
        this.adminRepository = adminRepository;
    }
    // 매장 정보 수정 메서드
    public void updateStore(Long store_id, Store updatedStore) {
        // 기존 매장 정보를 가져와서 업데이트된 속성 설정하고 저장
        Store existingStore = storeRepository.findById(store_id).orElse(null);
        if (existingStore != null) {
            existingStore.setStore_name(updatedStore.getStore_name());
            existingStore.setStore_address(updatedStore.getStore_address());
            existingStore.setStore_category(updatedStore.getStore_category());
            existingStore.setStore_phone(updatedStore.getStore_phone());
            storeRepository.save(existingStore);
        }
    }
    // 매장 추가 메서드
    public ResponseEntity<String> addStore(String admin_id, Store store) {

        Admin admin = adminRepository.findById(admin_id).orElse(null);

        if (admin != null) {
            // 새로운 매장 생성
            Store newStore = new Store();
            newStore.setStore_name(store.getStore_name());
            newStore.setStore_address(store.getStore_address());
            newStore.setStore_category(store.getStore_category());
            newStore.setStore_phone(store.getStore_phone());
            newStore.setAdmin(admin);
            // 새로운 매장을 저장
            storeRepository.save(newStore);
            return ResponseEntity.ok("매장 추가 성공");
        }
        return null;
    }

    public List<Menu> getAllMenusForStore(Long store_id) {
        Store store = storeRepository.findById(store_id).orElse(null);

        if (store != null) {
            return store.getMenus();
        } else {
            return Collections.emptyList();
        }
    }
    // 매장 삭제 메서드
    public void deleteStore(Long store_id) {
        storeRepository.deleteById(store_id);
    }
}