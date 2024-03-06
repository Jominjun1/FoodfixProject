package com.project.foodfix.service.Store;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;

import java.util.List;

public interface StoreService {
    // 카테고리, 매장 이름, 메뉴 이름을 기반으로 포장 가능한 매장을 검색하는 메서드
    List<PackableStoreDTO> searchPackableStores(String category, String storeName, String menuName);

    // 카테고리와 매장 이름을 기반으로 예약 가능한 매장을 검색하는 메서드
    List<ReservableStoreDTO> searchReservableStores(String category, String storeName);
}