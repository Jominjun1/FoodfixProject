package com.project.foodfix.service.Store;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;
import com.project.foodfix.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public List<PackableStoreDTO> searchPackableStores(String category, String storeName, String menuName) {
        // StoreRepository에서 적절한 메서드 호출하여 PackableStoreDTO 리스트 반환
        return storeRepository.findPackableStores(category, storeName, menuName);
    }

    @Override
    public List<ReservableStoreDTO> searchReservableStores(String category, String storeName) {
        // StoreRepository에서 적절한 메서드 호출하여 ReservableStoreDTO 리스트 반환
        return storeRepository.findStoresWithReservation(category, storeName);
    }
}