package com.project.foodfix.repository;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;
import com.project.foodfix.model.Store;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@SuppressWarnings("ALL")
public interface StoreRepository extends JpaRepository<Store, Long> {

    // 포장 가능 매장 반환
    @Transactional
    @Modifying
    @Query("SELECT DISTINCT new com.project.foodfix.model.DTO.PackableStoreDTO(s.store_id, s.photo.imagePath, s.store_name, s.store_category, s.minimumTime, s.openTime, s.closeTime) " +
            "FROM Store s " +
            "JOIN s.menus m " +
            "WHERE (:store_category IS NULL OR s.store_category = :store_category) " +
            "AND (:store_name IS NULL OR s.store_name LIKE %:store_name%) " +
            "AND (:menu_name IS NULL OR m.menu_name = :menu_name)")
    List<PackableStoreDTO> findPackableStores(@Param("store_category") String category, @Param("store_name") String store_name, @Param("menu_name") String menu_name);

    // 예약 가능 매장 반환
    @Transactional
    @Modifying
    @Query("SELECT DISTINCT new com.project.foodfix.model.DTO.ReservableStoreDTO(s.store_id, s.photo.imagePath, s.store_name, s.store_category, s.openTime, s.closeTime) " +
            "FROM Store s " +
            "JOIN s.menus m " +
            "WHERE (:store_category IS NULL OR s.store_category = :store_category) " +
            "AND (:store_name IS NULL OR s.store_name LIKE %:store_name%) " +
            "AND (:menu_name IS NULL OR m.menu_name = :menu_name)" +
            "AND s.res_status = '1'")
    List<ReservableStoreDTO> findStoresWithReservation(@Param("store_category") String store_category, @Param("store_name") String store_name, @Param("menu_name") String menu_name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Store s WHERE s.store_id = :store_id")
    void deleteById(Long store_id);

    @Transactional
    @Modifying
    @Query("SELECT s FROM Store s WHERE s.admin.admin_id = :admin_id")
    List<Store> findByAdminId(String admin_id);
}