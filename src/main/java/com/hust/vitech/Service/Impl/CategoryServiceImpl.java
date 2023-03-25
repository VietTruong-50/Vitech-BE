package com.hust.vitech.Service.Impl;

import com.hust.vitech.Constants.ErrorCode;
import com.hust.vitech.Exception.ApiException;
import com.hust.vitech.Exception.CustomException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createNewCategory(CategoryRequest categoryRequest) {
            Category category = new Category();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            category.setCategoryImageByte(categoryRequest.getCategoryImageByte());

            return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest categoryRequest) throws CustomException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_EXIST));

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setCategoryImageByte(categoryRequest.getCategoryImageByte());

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).map(
                category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().build();
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Category getCategoryById(Long id) throws CustomException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_EXIST));
    }

    @Override
    public Page<Category> getAllCategory(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return categoryRepository.findAll(pageable);
    }
}
