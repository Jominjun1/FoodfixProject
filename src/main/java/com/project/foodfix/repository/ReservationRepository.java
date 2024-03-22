package com.project.foodfix.repository;

import com.project.foodfix.model.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
@SuppressWarnings("ALL")
public interface ReservationRepository extends JpaRepository<Reservation ,Long> {

    @Transactional
    @Modifying
    @Query("SELECT r FROM Reservation r where r.store.id = :store_id")
    List<Reservation> findByStoreId(@Param("store_id")Long store_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.store.store_id = :store_id")
    void deleteByStoreId(@Param("store_id") Long store_id);
}
