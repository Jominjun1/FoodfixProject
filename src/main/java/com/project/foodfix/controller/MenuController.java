package com.project.foodfix.controller;

import com.project.foodfix.model.Menu;
import com.project.foodfix.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    // 새로운 메뉴 등록 API
    @PostMapping("/store/{store_id}/create")
    public void createMenus(@PathVariable("store_id") Long store_id, @RequestBody List<Menu> menus) {
        // 메뉴에 속한 매장을 설정하고 저장
        menuService.createMenus(store_id, menus);
    }
    // 메뉴 정보 수정 API
    @PutMapping("/update/{menu_id}")
    public void updateMenu(@PathVariable("menu_id") Long menu_id, @RequestBody Menu updatedMenu) {
        // 기존 메뉴 정보를 가져와서 업데이트된 속성 설정하고 저장
        menuService.updateMenu(menu_id, updatedMenu);
    }
    // 메뉴 삭제 API
    @DeleteMapping("/delete/{menu_id}")
    public void deleteMenu(@PathVariable("menu_id") Long menu_id) {
        menuService.deleteMenu(menu_id);
    }
}