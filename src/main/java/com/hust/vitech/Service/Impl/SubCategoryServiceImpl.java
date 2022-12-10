package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.SubCategory;
import com.hust.vitech.Repository.SubCategoryRepository;
import com.hust.vitech.Repository.CategoryRepository;
import com.hust.vitech.Request.SubCategoryRequest;
import com.hust.vitech.Service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public SubCategory createNewSubCategory(SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = new SubCategory();

        if (subCategoryRequest.getCategoryId() != null) {
            subCategory.setCategory(
                    categoryRepository.findById(subCategoryRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        return subCategoryRepository.save(subCategoryRequest.toSubCategory(subCategory));
    }

    @Override
    public Page<SubCategory> getSubCategoryData(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return subCategoryRepository.findAll(pageable);
    }

    @Override
    public List<SubCategory> getSubCategoryData() {
        return subCategoryRepository.findAll();
    }

    @Override
    public List<SubCategory> getSubCategoryDataByCategory(String name) {
        return subCategoryRepository.findAllByCategory_Name(name);
    }

    @Override
    public SubCategory updateSubCategory(Long id, SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        if (subCategoryRequest.getCategoryId() != null) {
            subCategory.setCategory(
                    categoryRepository.findById(subCategoryRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        return subCategoryRepository.save(subCategoryRequest.toSubCategory(subCategory));
    }

    @Override
    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));
    }
}
