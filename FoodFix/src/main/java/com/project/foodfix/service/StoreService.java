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
    public void updateStore(String admin_id, Long store_id, Store updatedStore) {
        // 기존 매장 정보를 가져옵니다.
        Store existingStore = storeRepository.findById(store_id).orElse(null);
        if (existingStore != null) {
            // 업데이트된 속성이 null이 아닌 경우에만 업데이트를 수행합니다.
            if (updatedStore.getStore_name() != null) {
                existingStore.setStore_name(updatedStore.getStore_name());
            }
            if (updatedStore.getStore_address() != null) {
                existingStore.setStore_address(updatedStore.getStore_address());
            }
            if (updatedStore.getStore_category() != null) {
                existingStore.setStore_category(updatedStore.getStore_category());
            }
            if (updatedStore.getStore_phone() != null) {
                existingStore.setStore_phone(updatedStore.getStore_phone());
            }
            if (updatedStore.getMinimumTime() != null) {
                existingStore.setMinimumTime(updatedStore.getMinimumTime());
            }
            if (updatedStore.getOpenTime() != null) {
                existingStore.setOpenTime(updatedStore.getOpenTime());
            }
            if (updatedStore.getCloseTime() != null) {
                existingStore.setCloseTime(updatedStore.getCloseTime());
            }
            if (updatedStore.getRes_max() != null) {
                existingStore.setRes_max(updatedStore.getRes_max());
            }
            if (updatedStore.getRes_status() != null) {
                existingStore.setRes_status(updatedStore.getRes_status());
            }
            // 업데이트된 매장 정보를 저장합니다.
            storeRepository.save(existingStore);
        }
    }

    // 매장 추가 메서드
    public ResponseEntity<String> addStore(String admin_id, Store store) {
        Admin admin = adminRepository.findById(admin_id).orElse(null);

        if (admin != null) {
            // 새로운 매장 생성
            Store newStore = new Store();

            // 복사를 통해 코드 중복을 줄입니다.
            storeProperties(store, newStore);

            newStore.setAdmin(admin);

            // 새로운 매장을 저장
            storeRepository.save(newStore);
            return ResponseEntity.ok("매장 추가 성공");
        }
        return null;
    }
    // 해당 매장의 모든 메뉴 조회 메서드
    public List<Menu> getAllMenusForStore(Long store_id) {
        Store store = storeRepository.findById(store_id).orElse(null);

        if (store != null) {
            return store.getMenus();
        } else {
            return Collections.emptyList();
        }
    }
    // 매장 삭제 메서드
    public void deleteStore(String admin_id, Long store_id) {
        storeRepository.deleteById(store_id);
    }
    // 메서드를 통해 코드 중복 제거
    private void storeProperties(Store source, Store target) {
        target.setStore_name(source.getStore_name());
        target.setStore_address(source.getStore_address());
        target.setStore_category(source.getStore_category());
        target.setStore_phone(source.getStore_phone());
        target.setMinimumTime(source.getMinimumTime());
        target.setOpenTime(source.getOpenTime());
        target.setCloseTime(source.getCloseTime());
        target.setRes_status(source.getRes_status());
        target.setRes_max(source.getRes_max());
    }
}