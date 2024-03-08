package com.project.foodfix.repository;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;
import com.project.foodfix.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // PackableStoreDTO 반환하는 메서드들
    @Query("SELECT DISTINCT new com.project.foodfix.model.DTO.PackableStoreDTO(s.store_image, s.store_name, s.store_category, s.minimumTime) " +
            "FROM Store s " +
            "JOIN s.menus m " +
            "WHERE (:store_category IS NULL OR s.store_category = :store_category) " +
            "AND (:store_name IS NULL OR s.store_name LIKE %:store_name%) " +
            "AND (:menu_name IS NULL OR m.menu_name = :menu_name)")
    List<PackableStoreDTO> findPackableStores(@Param("store_category") String category, @Param("store_name") String store_name, @Param("menu_name") String menu_name);

    // ReservableStoreDTO 반환하는 메서드들
    @Query("SELECT DISTINCT new com.project.foodfix.model.DTO.ReservableStoreDTO(s.store_name, s.store_image, s.store_category) " +
            "FROM Store s " +
            "JOIN s.menus m " +
            "WHERE (:store_category IS NULL OR s.store_category = :store_category) " +
            "AND (:store_name IS NULL OR s.store_name LIKE %:store_name%) " +
            "AND (:menu_name IS NULL OR m.menu_name = :menu_name)" +
            "AND s.res_status = '1'")
    List<ReservableStoreDTO> findStoresWithReservation
    (@Param("store_category") String store_category, @Param("store_name") String store_name , @Param("menu_name") String menu_name);

}