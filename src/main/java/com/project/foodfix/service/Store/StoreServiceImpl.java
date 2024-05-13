package com.project.foodfix.service.Store;

import com.project.foodfix.model.*;
import com.project.foodfix.model.DTO.*;
import com.project.foodfix.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PackingRepository packingRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, ReservationRepository reservationRepository, UserRepository userRepository, PackingRepository packingRepository, MenuItemRepository menuItemRepository) {
        this.storeRepository = storeRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.packingRepository = packingRepository;
        this.menuItemRepository = menuItemRepository;
    }
    /////// 포장 /////////
    @Override
    public List<PackingDTO> packingStore(PackingDTO packingDTO) {
        User user = userRepository.findById(packingDTO.getUser_id()).orElse(null);
        Store store = storeRepository.findById(packingDTO.getStore_id()).orElse(null);

        Packing packing = new Packing();
        packing.setUser(user);
        packing.setStore(store);
        packing.setPacking_date(packingDTO.getPacking_date());
        packing.setPacking_time(packingDTO.getPacking_time());
        packing.setUser_comments(packingDTO.getUser_comments());
        packing.setPayment_type(packingDTO.getPayment_type());
        packing.setPacking_status("0");

        Packing savedPacking = packingRepository.save(packing);

        List<MenuItemDTO> menuItemDTOList = packingDTO.getMenuItemDTOList();

        // 메뉴 정보 설정
        for (MenuItemDTO menuItemDTO : menuItemDTOList) {
            double totalMenuPrice = 0.0;
            MenuItem menuItem = new MenuItem();
            menuItem.setMenu_name(menuItemDTO.getMenu_name());
            menuItem.setMenu_price(menuItemDTO.getMenu_price());
            menuItem.setQuantity(menuItemDTO.getQuantity());
            menuItem.setPacking(savedPacking);
            // 메뉴 가격 누적
            totalMenuPrice += menuItem.getMenu_price() * menuItem.getQuantity();
            menuItem.setTotalPrice(totalMenuPrice);
            menuItemRepository.save(menuItem);
        }

        // 저장된 예약을 반환
        return Collections.singletonList(returnPackingDTO(savedPacking));
    }

    @Override
    public List<PackingDTO> getPacking(Long store_id) {
        List<Packing> packings = packingRepository.findByStoreId(store_id);

        // 시간순으로 정렬
        packings.sort(Comparator.comparing(Packing::getPacking_time).reversed());

        // 리스트로 변환 후 반환
        List<PackingDTO> packingDTOS = new ArrayList<>();
        for (Packing packing : packings) {
            packingDTOS.add(returnPackingDTO(packing));
        }
        return packingDTOS;
    }

    @Override
    public void updatePacking(PackingDTO packingDTO) {
        String newStatus = packingDTO.getPacking_status();

        // 매장 테이블 업데이트
        Packing packing = packingRepository.findById(packingDTO.getPacking_id()).orElse(null);
        if (packing != null) {
            packing.setPacking_status(newStatus);
            packingRepository.save(packing);
        }
    }

    @Override
    public void updateReservation(ReservationDTO reservationDTO) {
        String newStatus = reservationDTO.getReservation_status();

        // 매장 테이블 업데이트
        Reservation reservation = reservationRepository.findById(reservationDTO.getReservation_id()).orElse(null);
        if (reservation != null) {
            reservation.setReservation_status(newStatus);
            reservationRepository.save(reservation);
        }
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

        // ISO 8601 형식 문자열 -> LocalDateTime 변환
        reservation.setReservation_date(reservationDTO.getReservation_date());
        reservation.setReservation_time(reservationDTO.getReservation_time());
        reservation.setNum_people(reservationDTO.getPeople_cnt());
        reservation.setUser_comments(reservationDTO.getUser_comments());
        reservation.setReservation_status("0"); // 예약 대기 상태로 초기화

        // 예약에 필요한 사용자 정보 설정
        reservation.setUser(user);

        // 예약에 필요한 매장 정보 설정
        Store store = new Store();
        store.setStore_id(reservationDTO.getStore_id()); // 예약할 매장의 ID를 설정
        reservation.setStore(store);

        Reservation savedReservation = reservationRepository.save(reservation);

        //  ReservationDTO 변환 후 반환
        return Collections.singletonList(returnReservationDTO(savedReservation));
    }
    public List<ReservationDTO> getReservations(Long store_id) {
        List<Reservation> reservations = reservationRepository.findByStoreId(store_id);

        // 시간순으로 정렬
        reservations.sort(Comparator.comparing(Reservation::getReservation_time).reversed());

        // 리스트로 변환 후 반환
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
        reservationDTO.setStore_name(reservation.getStore().getStore_name());

        // 예약 날짜 설정
        reservationDTO.setReservation_date(reservation.getReservation_date());
        reservationDTO.setReservation_time(reservation.getReservation_time());

        reservationDTO.setStore_name(reservation.getStore().getStore_name());
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
        packingDTO.setStore_name(packing.getStore().getStore_name());

        // 메뉴 정보 설정
        List<MenuItemDTO> menuItemDTOList = new ArrayList<>();
        for (MenuItem menuItem : packing.getMenuitem()) {
            MenuItemDTO menuItemDTO = new MenuItemDTO(
                    menuItem.getMenuitem_id(),
                    menuItem.getMenu_price(),
                    menuItem.getTotalPrice(),
                    menuItem.getMenu_name(),
                    menuItem.getQuantity(),
                    menuItem.getPacking()
            );
            menuItemDTOList.add(menuItemDTO);
        }
        packingDTO.setStore_name(packing.getStore().getStore_name());
        packingDTO.setMenuItemDTOList(menuItemDTOList);
        return packingDTO;
    }
    public PackingDTO PackingList(Packing packing) {
        PackingDTO packingDTO = new PackingDTO();

        packingDTO.setPacking_id(packing.getPacking_id());
        packingDTO.setPacking_status(packing.getPacking_status());
        packingDTO.setPacking_time(packing.getPacking_time());
        packingDTO.setPacking_date(packing.getPacking_date());
        packingDTO.setUser_comments(packing.getUser_comments());
        packingDTO.setPayment_type(packing.getPayment_type());

        packingDTO.setUser_id(packing.getUser().getUser_id());
        packingDTO.setUser_phone(packing.getUser().getUser_phone());


        packingDTO.setStore_id(packing.getStore().getStore_id());
        packingDTO.setStore_phone(packing.getStore().getStore_phone());
        packingDTO.setStore_name(packing.getStore().getStore_name());

        List<MenuItemDTO> menuItemDTOList = new ArrayList<>();
        List<MenuItem> menuItems = packing.getMenuitem();
        for (MenuItem menuItem : menuItems) {
            MenuItemDTO menuItemDTO = new MenuItemDTO();
            menuItemDTO.setMenu_id(menuItem.getMenuitem_id());
            menuItemDTO.setMenu_name(menuItem.getMenu_name());
            menuItemDTO.setMenu_price(menuItem.getMenu_price());
            menuItemDTO.setTotalPrice(menuItem.getTotalPrice());
            menuItemDTO.setQuantity(menuItem.getQuantity());
            menuItemDTOList.add(menuItemDTO);
        }
        packingDTO.setStore_name(packing.getStore().getStore_name());
        packingDTO.setMenuItemDTOList(menuItemDTOList);

        return packingDTO;
    }
}