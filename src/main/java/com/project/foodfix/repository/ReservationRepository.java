package com.project.foodfix.repository;

import com.project.foodfix.model.Reservation;
import com.project.foodfix.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
public interface ReservationRepository extends JpaRepository<Reservation , Long> {
    // 매장과 예약 시간 사이에 예약된 팀 수를 조회하는 메서드
    long countByStoreAndReservationDateTimeBetween(Store store, LocalDateTime startTime, LocalDateTime endTime);
}
