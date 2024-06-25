package org.lafresca.lafrescabackend.Services;

import org.lafresca.lafrescabackend.Exceptions.ResourceNotFoundException;
import org.lafresca.lafrescabackend.Models.Menu;
import org.lafresca.lafrescabackend.Repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // Add new menu item
//    public String addNewMenu(Menu menu) {
//        String error = null;
//
//        if (menu.getName() == null || menu.getName().isEmpty()) {
//            error = "Please enter name";
//        } else if (menu.getDescription() == null || menu.getDescription().isEmpty()) {
//            error = "Please enter description";
//        } else if (menu.getPrice() <= 0) {
//            error = "Please enter a valid price";
//        } else if (menu.getImage() == null || menu.getImage().isEmpty()) {
//            error = "Please upload image";
//        } else if (menu.getCafeId() == null || menu.getCafeId().isEmpty()) {
//            error = "Please enter cafe id";
//        } else if (menu.getAvailable() < 0 || menu.getAvailable() > 1) {
//            error = "Invalid value for availability";
//        } else if (menu.getDeleted() < 0 || menu.getDeleted() > 1) {
//            error = "Invalid value for deleted status";
//        } else if (menu.getFeatures() == null || menu.getFeatures().isEmpty()) {
//            error = "Please enter at least one feature";
//        }
//
//        if (error == null) {
//            menuRepository.save(menu);
//        }
//        return error;
//    }


    // Retrieve all menu items
    public List<Menu> getMenus() {
        return menuRepository.findAll();
    }

    // Update menu item
//    public void updateMenu(String id, Menu menu) {
//        Menu existingMenu = menuRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id " + id));
//
//        if (menu.getName() != null && !menu.getName().isEmpty()) {
//            existingMenu.setName(menu.getName());
//        } else if (menu.getDescription() != null && !menu.getDescription().isEmpty()) {
//            existingMenu.setDescription(menu.getDescription());
//        } else if (menu.getPrice() != null && menu.getPrice() >= 0) {
//            existingMenu.setPrice(menu.getPrice());
//        } else if (menu.getImage() != null && !menu.getImage().isEmpty()) {
//            existingMenu.setImage(menu.getImage());
//        } else if (menu.getCafeId() != null && !menu.getCafeId().isEmpty()) {
//            existingMenu.setCafeId(menu.getCafeId());
//        } else if (menu.getAvailable() == 0 || menu.getAvailable() == 1) {
//            existingMenu.setAvailable(menu.getAvailable());
//        } else if (menu.getDeleted() == 0 || menu.getDeleted() == 1) {
//            existingMenu.setDeleted(menu.getDeleted());
//        }
//
//        existingMenu.setFeatures(menu.getFeatures());
//
//        menuRepository.save(existingMenu);
//    }

    // Delete menu item by id
    public void deleteMenu(String id) {
        menuRepository.deleteById(id);
    }

    // Search menu item
    public Optional<Menu> getMenu(String id) {
        return menuRepository.findById(id);
    }
}
