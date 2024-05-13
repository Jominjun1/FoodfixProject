package com.project.foodfix.repository;

import com.project.foodfix.model.Packing;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("ALL")
public interface PackingRepository extends JpaRepository<Packing,Long> {

    @Transactional
    @Modifying
    @Query("SELECT p FROM Packing p where p.store.id = :store_id")
    List<Packing> findByStoreId(@Param("store_id")Long store_id);

    @Transactional
    @Modifying
    @Query("SELECT p FROM Packing p where p.user.id = :user_id")
    List<Packing> findByUserId(@Param("user_id")String user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Packing p WHERE p.store.store_id = :store_id")
    void deleteByStoreId(@Param("store_id") Long store_id);
}
