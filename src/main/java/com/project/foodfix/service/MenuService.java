package com.project.foodfix.service;

import com.project.foodfix.model.Menu;
import com.project.foodfix.model.Store;
import com.project.foodfix.repository.MenuRepository;
import com.project.foodfix.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    @Autowired
    public MenuService(MenuRepository menuRepository, StoreRepository storeRepository) {
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
    }
    // 해당 매장에서 메뉴 생성
    public void createMenus(Long store_id, List<Menu> menus) {
        Store store = storeRepository.findById(store_id).orElse(null);
        if (store != null) {
            for (Menu menu : menus) {
                // 매장에 메뉴 리스트 저장
                menu.setStore(store);
                // 메뉴 저장
                menuRepository.save(menu);
            }
        }
    }
    // 메뉴 정보 수정 메서드
    public void updateMenu(Long menu_id, Menu updatedMenu) {
        // 기존 메뉴 정보를 가져와서 업데이트된 속성 설정하고 저장
        Menu existingMenu = menuRepository.findById(menu_id).orElse(null);
        if (existingMenu != null) {
            existingMenu.setMenu_name(updatedMenu.getMenu_name());
            existingMenu.setMenu_price(updatedMenu.getMenu_price());
            existingMenu.setExplanation(updatedMenu.getExplanation());
            existingMenu.setMenu_image(updatedMenu.getMenu_image());
            menuRepository.save(existingMenu);
        }
    }
    // 메뉴 삭제 메서드
    public void deleteMenu(Long menu_id) {
        menuRepository.deleteById(menu_id);
    }
}
