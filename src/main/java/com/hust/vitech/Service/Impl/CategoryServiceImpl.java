package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Category;
import com.hust.vitech.Repository.CategoryRepository;
import com.hust.vitech.Request.CategoryRequest;
import com.hust.vitech.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createNewCategory(CategoryRequest categoryRequest) {
            Category category = new Category();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            category.setParent_id(categoryRequest.getParent_id());

            return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setParent_id(categoryRequest.getParent_id());

        return categoryRepository.save(category);
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long id) {
        return categoryRepository.findById(id).map(
                category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().build();
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Page<Category> getAllCategory(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return categoryRepository.findAll(pageable);
    }
}
