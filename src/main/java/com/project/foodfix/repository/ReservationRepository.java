package com.project.foodfix.repository;

import com.project.foodfix.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
@SuppressWarnings("ALL")
public interface ReservationRepository extends JpaRepository<Reservation ,Long> {
    @Query("SELECT r FROM Reservation r ORDER BY r.reservation_time DESC")
    // 예약 주문 내역 조회
    List<Reservation> findAllByOrderByReservationTimeDesc();
}
