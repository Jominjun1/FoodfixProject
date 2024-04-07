package com.project.foodfix.service.Store;

import com.project.foodfix.model.*;
import com.project.foodfix.model.DTO.*;
import com.project.foodfix.repository.PackingRepository;
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
    private final PackingRepository packingRepository;
    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, ReservationRepository reservationRepository, UserRepository userRepository, PackingRepository packingRepository) {
        this.storeRepository = storeRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.packingRepository = packingRepository;
    }
    /////// 포장 /////////
    @Override
    public List<PackingDTO> packingStore(PackingDTO packingDTO) {
        double totalMenuPrice = 0.0; // 메뉴 총 가격
        User user = userRepository.findById(packingDTO.getUser_id()).orElse(null);

        Packing packing = new Packing();

        packing.setPacking_date(packingDTO.getPacking_date());
        packing.setPacking_time(packingDTO.getPacking_time());
        packing.setUser_comments(packingDTO.getUser_comments());
        packing.setPayment_type(packingDTO.getPayment_type());
        packing.setPacking_status("0");

        // 메뉴 정보 설정
        for (MenuItemDTO menuItemDTO : packingDTO.getMenuItemDTOList()) {
            MenuItem menuItem = new MenuItem();
            menuItem.setMenu_name(menuItemDTO.getMenu_name());
            menuItem.setMenu_price(menuItemDTO.getMenu_price());
            menuItem.setQuantity(menuItemDTO.getQuantity());
            menuItem.setPacking(packing);

            packing.getMenus().add(menuItem); // 포장에 메뉴 정보 추가
            // 메뉴 가격 누적
            totalMenuPrice += menuItem.getMenu_price() * menuItem.getQuantity();
        }
        packing.setTotalPrice(totalMenuPrice);

        // 포장에 필요한 사용자 정보 설정
        packing.setUser(user); // 저장된 사용자 정보 설정

        // 포장에 필요한 매장 정보 설정
        Store store = new Store();
        store.setStore_id(packingDTO.getStore_id()); // 포장할 매장의 ID를 설정
        packing.setStore(store);

        // 저장된 예약을 반환
        Packing savedPacking = packingRepository.save(packing);

        // 저장된 예약을 PackingDTO 반환
        return Collections.singletonList(returnPackingDTO(savedPacking));
    }
    @Override
    public List<PackingDTO> getPackingByStoreId(Long store_id) {
        // 매장 ID를 이용하여 해당 매장의 예약 내역을 조회
        List<Packing> packings = packingRepository.findByStoreId(store_id);

        // 조회된 예약 내역을 시간순으로 정렬
        packings.sort(Comparator.comparing(Packing::getPacking_time).reversed());

        // 조회된 예약 내역을 ReservationDTO 리스트로 변환 후 반환
        List<PackingDTO> packingDTOS = new ArrayList<>();
        for (Packing packing : packings) {
            packingDTOS.add(returnPackingDTO(packing));
        }
        return packingDTOS;
    }
    /////// 검색 /////////
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
    /////// 예약 /////////
    @Override
    public List<ReservationDTO> reservationStore(ReservationDTO reservationDTO) {
        User user = userRepository.findById(reservationDTO.getUser_id()).orElse(null);
        // ReservationDTO -> Reservation 엔터티로 변환
        Reservation reservation = new Reservation();

        // 예약 시간을 ISO 8601 형식의 문자열에서 LocalDateTime 변환
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

        // 저장된 예약을 ReservationDTO 변환 후 반환
        return Collections.singletonList(returnReservationDTO(savedReservation));
    }
    public List<ReservationDTO> getReservationsByStoreId(Long store_id) {
        // 매장 ID를 이용하여 해당 매장의 예약 내역을 조회
        List<Reservation> reservations = reservationRepository.findByStoreId(store_id);

        // 조회된 예약 내역을 시간순으로 정렬
        reservations.sort(Comparator.comparing(Reservation::getReservation_time).reversed());

        // 조회된 예약 내역을 ReservationDTO 리스트로 변환 후 반환
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOs.add(returnReservationDTO(reservation));
        }
        return reservationDTOs;
    }

    // Reservation -> ReservationDTO 변환
    private ReservationDTO returnReservationDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();

        // 예약 정보 설정
        reservationDTO.setReservation_id(reservation.getReservation_id());
        reservationDTO.setReservation_status(reservation.getReservation_status());
        reservationDTO.setReservation_date(reservation.getReservation_date());
        reservationDTO.setReservation_time(reservation.getReservation_time());
        reservationDTO.setPeople_cnt(reservation.getNum_people());
        reservationDTO.setUser_comments(reservation.getUser_comments());

        // 사용자 정보 설정
        reservationDTO.setUser_id(reservation.getUser().getUser_id());
        reservationDTO.setUser_phone(reservation.getUser().getUser_phone());

        // 매장 정보 설정
        reservationDTO.setStore_id(reservation.getStore().getStore_id());
        reservationDTO.setStore_phone(reservation.getStore().getStore_phone());

        // 예약 날짜 설정
        reservationDTO.setReservation_date(reservation.getReservation_date());
        reservationDTO.setReservation_time(reservation.getReservation_time());

        return reservationDTO;
    }
    // Packing -> PackingDTO 변환
    private PackingDTO returnPackingDTO(Packing packing) {
        PackingDTO packingDTO = new PackingDTO();

        // 포장 정보 설정
        packingDTO.setPacking_id(packing.getPacking_id());
        packingDTO.setPacking_status(packing.getPacking_status());
        packingDTO.setPacking_time(packing.getPacking_time());
        packingDTO.setPacking_date(packing.getPacking_date());
        packingDTO.setUser_comments(packing.getUser_comments());
        packingDTO.setPayment_type(packing.getPayment_type());

        // 사용자 정보 설정
        packingDTO.setUser_id(packing.getUser().getUser_id());
        packingDTO.setUser_phone(packing.getUser().getUser_phone());

        // 매장 정보 설정
        packingDTO.setStore_id(packing.getStore().getStore_id());
        packingDTO.setStore_phone(packing.getStore().getStore_phone());

        return packingDTO;
    }
}