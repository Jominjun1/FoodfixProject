package com.project.foodfix.service;

import com.project.foodfix.model.Menu;
import com.project.foodfix.model.OrderItem;
import com.project.foodfix.model.TakeoutOrder;
import com.project.foodfix.model.dto.TakeoutDTO;
import com.project.foodfix.repository.MenuRepository;
import com.project.foodfix.repository.TakeoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TakeoutService {
    private final TakeoutRepository takeoutRepository;
    private final MenuRepository menuRepository;
    @Autowired
    public TakeoutService(TakeoutRepository takeoutRepository, MenuRepository menuRepository) {
        this.takeoutRepository = takeoutRepository;
        this.menuRepository = menuRepository;
    }
    // 포장 주문을 처리하는 메서드
    public void placeTakeoutOrder(TakeoutDTO takeoutDTO) {
        // 현재 시간을 가져옵니다.
        LocalDateTime orderDateTime = LocalDateTime.now();
        // 예상 완료 시간을 초기화합니다.
        LocalDateTime completeTime ;
        // 사용자가 직접 주문 시간을 선택한 경우 처리
        if (takeoutDTO.getTimeStatus() == 1) {
            completeTime = orderDateTime.plusMinutes(takeoutDTO.getPreparationTimeMinutes());
        } else {
            // 사용자가 시간을 선택하지 않은 경우, 자동으로 TimeStatus를 1로 설정합니다.
            takeoutDTO.setTimeStatus(1);
            completeTime = orderDateTime.plusMinutes(takeoutDTO.getStore().getMinimumTime());
        }
        // TakeoutOrder 엔터티를 생성합니다.
        TakeoutOrder takeoutOrder = new TakeoutOrder();
        takeoutOrder.setOrderDateTime(orderDateTime);
        takeoutOrder.setEstimatedCompletionTime(completeTime);
        takeoutOrder.setRequirements(takeoutDTO.getRequirements());
        takeoutOrder.setTimeStatus(takeoutDTO.getTimeStatus());
        takeoutOrder.setOrderStatus(0);
        takeoutOrder.setPaymentStatus(takeoutDTO.getPaymentMethod() == 0 ? 0 : 1);
        takeoutOrder.setPaymentMethod(takeoutDTO.getPaymentMethod());
        // TimeStatus 따라 timeOrder 값을 설정합니다.
        if (takeoutDTO.getTimeStatus() == 1) {
            takeoutOrder.setTimeOrder(takeoutDTO.getPreparationTimeMinutes());
        } else {
            takeoutOrder.setTimeOrder(0);
        }
        double totalMenuPrice = 0;
        // 주문된 메뉴 항목들에 대한 처리
        for (TakeoutDTO.OrderItemDTO orderItemDTO : takeoutDTO.getOrderItems()) {
            // 주문된 메뉴 정보를 가져옵니다.
            Menu menu = menuRepository.findById(orderItemDTO.getMenu().getMenu_id()).orElse(null);

            OrderItem orderItem = new OrderItem();
            orderItem.setMenu(menu);
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setTakeoutOrder(takeoutOrder);
            takeoutOrder.getOrderItems().add(orderItem);
            // 주문 총액을 업데이트합니다.
            totalMenuPrice += orderItem.getMenu().getMenu_price() * orderItem.getQuantity();
        }
        takeoutOrder.setTotalMenuPrice(totalMenuPrice);
        takeoutOrder.setUser(takeoutDTO.getUser());
        takeoutOrder.setStore(takeoutDTO.getStore());
        // 생성된 TakeoutOrder 저장합니다.
        takeoutRepository.save(takeoutOrder);
    }
    // 주문 취소 메서드
    public void cancelTakeoutOrder(Long takeoutOrderId) throws Exception {
        TakeoutOrder takeoutOrder = takeoutRepository.findById(takeoutOrderId).orElse(null);

        if (takeoutOrder != null) {
            if (takeoutOrder.getOrderStatus() == 1 || takeoutOrder.getOrderStatus() == 2) {
                throw new Exception("주문이 접수 되어서 취소가 불가능합니다.");
            }

            takeoutOrder.setOrderStatus(3);
            takeoutRepository.save(takeoutOrder);
        }
    }
}
