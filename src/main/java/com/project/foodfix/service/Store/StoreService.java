package com.project.foodfix.service.Store;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.PackingDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;
import com.project.foodfix.model.DTO.ReservationDTO;

import java.util.List;

public interface StoreService {
    // 카테고리, 매장 이름, 메뉴 이름을 기반으로 포장 가능한 매장을 검색
    List<PackableStoreDTO> searchPackableStores(String store_category, String store_name, String menu_name);
    // 카테고리,  매장 이름 , 메뉴 이름을 기반으로 예약 가능한 매장을 검색
    List<ReservableStoreDTO> searchReservableStores(String store_category, String store_name,  String menu_name);
    // 매장 예약 기능
    List<ReservationDTO> reservationStore(ReservationDTO reservationDTO);
    // 매장 포장 기능
    List<PackingDTO> packingStore(PackingDTO packingStoreDTO);
    // 예약 내역 조회 기능
    List<ReservationDTO> getReservations(Long store_id);
    // 예약 주문 상태 변경
    void updateReservation(ReservationDTO reservationDTO);
    // 포장 내역 조회 기능
    List<PackingDTO> getPacking(Long store_id);
    // 포장 주문 상태 변경
    void updatePacking(PackingDTO packingDTO);

}