package com.project.foodfix.repository;

import com.project.foodfix.model.Menu;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // 매장에 속한 모든 메뉴 삭제
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM menu WHERE store_id = :store_id", nativeQuery = true)
    void deleteMenusByStoreId(@Param("store_id") Long store_id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.menu_id= :menu_id")
    void deleteByMenuId(Long menu_id);
}
