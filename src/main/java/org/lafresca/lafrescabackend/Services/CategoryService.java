package org.lafresca.lafrescabackend.Services;

import org.lafresca.lafrescabackend.Exceptions.ResourceNotFoundException;
import org.lafresca.lafrescabackend.Models.Category;
import org.lafresca.lafrescabackend.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }

    // Add New Category
    public String addNewCategory(Category category) {
        String error = null;

        if (category.getName() == null || category.getName().isEmpty()) {
            error = "Category name cannot be empty";
        }
        else if (category.getDescription() == null || category.getDescription().isEmpty()) {
            error = "Category description cannot be empty";
        }

        Category alreadyExisting = categoryRepository.findByName(category.getName());

        if (alreadyExisting != null) {
            error = "Category already exists";
        }

        if (error == null) {
            categoryRepository.save(category);
        }

        return error;
    }

    // Get All Categories
    public List<Category> getCategories() { return categoryRepository.findAll(); }

    // Search Category
    public Optional<Category> getCategory(String id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryRepository.findById(id);
    }

    // Delete Category
    public void deleteCategory(String id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.deleteById(id);
    }

    // Update Category
    public void updateCategory(String id, Category category) {
        Category existingCategory = categoryRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (category.getName() != null && !category.getName().isEmpty()) {
            existingCategory.setName(category.getName());
        }
        if (category.getDescription() != null && !category.getDescription().isEmpty()) {
            existingCategory.setDescription(category.getDescription());
        }

        categoryRepository.save(existingCategory);
    }
}
