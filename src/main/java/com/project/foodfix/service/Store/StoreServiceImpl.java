package com.project.foodfix.service.Store;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;
import com.project.foodfix.model.DTO.ReservationDTO;
import com.project.foodfix.model.Reservation;
import com.project.foodfix.model.Store;
import com.project.foodfix.model.User;
import com.project.foodfix.repository.ReservationRepository;
import com.project.foodfix.repository.StoreRepository;
import com.project.foodfix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;
    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, ReservationRepository reservationRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<PackableStoreDTO> searchPackableStores(String store_category, String store_name, String menu_name) {
        // PackableStoreDTO 리스트 반환
        return storeRepository.findPackableStores(store_category, store_name, menu_name);
    }

    @Override
    public List<ReservableStoreDTO> searchReservableStores(String store_category, String store_name, String menu_name) {
        // ReservableStoreDTO 리스트 반환
        return storeRepository.findStoresWithReservation(store_category, store_name, menu_name);
    }
    @Override
    public List<ReservationDTO> reservationStore(ReservationDTO reservationDTO) {
        User user = userRepository.findById(reservationDTO.getUser_id()).orElse(null);
        // ReservationDTO에서 Reservation 엔터티로 변환
        Reservation reservation = new Reservation();

        // 예약 시간을 ISO 8601 형식의 문자열에서 LocalDateTime으로 변환
        reservation.setReservation_date(reservationDTO.getReservation_date());
        reservation.setReservation_time(reservationDTO.getReservation_time());
        reservation.setNum_people(reservationDTO.getPeople_cnt());
        reservation.setUser_comments(reservationDTO.getUser_comments());
        reservation.setReservation_status("0"); // 예약 대기 상태로 초기화

        // 예약에 필요한 사용자 정보 설정
        reservation.setUser(user); // 저장된 사용자 정보 설정

        // 예약에 필요한 매장 정보 설정
        Store store = new Store();
        store.setStore_id(reservationDTO.getStore_id()); // 예약할 매장의 ID를 설정
        reservation.setStore(store);

        // 저장된 예약을 반환
        Reservation savedReservation = reservationRepository.save(reservation);

        // 저장된 예약을 ReservationDTO로 변환하여 반환
        return Collections.singletonList(returnReservationDTO(savedReservation));
    }

    public List<ReservationDTO> getReservationsByStoreId(Long store_id) {
        // 매장 ID를 이용하여 해당 매장의 예약 내역을 조회
        List<Reservation> reservations = reservationRepository.findByStoreId(store_id);

        // 조회된 예약 내역을 시간순으로 정렬
        reservations.sort(Comparator.comparing(Reservation::getReservation_time).reversed());

        // 조회된 예약 내역을 ReservationDTO 리스트로 변환하여 반환
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOs.add(returnReservationDTO(reservation));
        }
        return reservationDTOs;
    }

    // Reservation 엔터티를 ReservationDTO로 변환
    private ReservationDTO returnReservationDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();

        // 예약 정보 설정
        reservationDTO.setReservation_date(reservation.getReservation_date());
        reservationDTO.setReservation_time(reservation.getReservation_time());
        reservationDTO.setPeople_cnt(reservation.getNum_people());
        reservationDTO.setUser_comments(reservation.getUser_comments());

        // 사용자 정보 설정
        reservationDTO.setUser_id(reservation.getUser().getUser_id());
        reservationDTO.setUser_phone(reservation.getUser().getUser_phone()); // 필요한 경우 사용자 전화번호도 설정

        // 매장 정보 설정
        reservationDTO.setStore_id(reservation.getStore().getStore_id());
        reservationDTO.setStore_phone(reservation.getStore().getStore_phone()); // 필요한 경우 매장 전화번호도 설정

        // 예약 날짜 설정
        reservationDTO.setReservation_date(reservation.getReservation_date());
        reservationDTO.setReservation_time(reservation.getReservation_time());

        return reservationDTO;
    }

}