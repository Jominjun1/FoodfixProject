package com.project.foodfix.service;

import com.project.foodfix.model.Reservation;
import com.project.foodfix.model.Store;
import com.project.foodfix.model.User;
import com.project.foodfix.repository.ReservationRepository;
import com.project.foodfix.repository.StoreRepository;
import com.project.foodfix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.*;

@Service
public class ReservationService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(UserRepository userRepository, StoreRepository storeRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.reservationRepository = reservationRepository;
    }

    // 예약 가능한 매장 검색 및 예약 메서드
    public void createReservation(String userId, Long storeId, LocalDateTime userSelectTime) {
        // 단계 1: 사용자 및 매장 엔터티 검색
        User user = userRepository.findById(userId).orElse(null);
        Store store = storeRepository.findById(storeId).orElse(null);

        if (user != null && store != null && store.getRes_status().equals("1")) {
            // 단계 2: 매장이 예약 가능한지 확인
            if (store.getRes_max() > 0 && checkTimeReservation(store, userSelectTime)) {
                // 단계 3: 예약 엔터티 생성
                Reservation reservation = new Reservation();
                reservation.setReservation_Date(LocalDateTime.now());
                reservation.setReservationDateTime(userSelectTime);
                reservation.setRes_requirements("");
                reservation.setRes_Status(0); // 0은 '접수중'
                reservation.setUser(user);
                reservation.setStore(store);

                // 단계 4: 예약 엔터티 저장
                reservationRepository.save(reservation);

                // 단계 5: 매장의 남은 예약 가능팀 수 업데이트
                store.setRes_max(store.getRes_max() - 1);
                storeRepository.save(store);
            }
        }
    }
    // 예약 가능한 시간을 확인해주는 메서드
    private boolean checkTimeReservation(Store store, LocalDateTime userSelectTime) {
        LocalTime openTime = store.getOpenTime();
        LocalTime closeTime = store.getCloseTime();
        int maxTeams = store.getRes_max();
        Duration timeSlot = Duration.ofHours(1);
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);

        while (startTime.isBefore(userSelectTime) && startTime.plus(timeSlot).isBefore(LocalDateTime.of(userSelectTime.toLocalDate(), closeTime))) {
            long reservedTeams = reservationRepository.countByStoreAndReservationDateTimeBetween(store, startTime, startTime.plus(timeSlot));
            if (reservedTeams < maxTeams) {
                return true;
            }
            startTime = startTime.plus(timeSlot);
        }
        return false;
    }

}
