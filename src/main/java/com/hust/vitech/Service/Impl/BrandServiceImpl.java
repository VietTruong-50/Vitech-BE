package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Brand;
import com.hust.vitech.Repository.BrandRepository;
import com.hust.vitech.Repository.CategoryRepository;
import com.hust.vitech.Request.BrandRequest;
import com.hust.vitech.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Brand createNewBrand(BrandRequest brandRequest) {
        Brand brand = new Brand();

        if (brandRequest.getCategoryId() != null) {
            brand.setCategory(
                    categoryRepository.findById(brandRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        return brandRepository.save(brandRequest.toBrand(brand));
    }

    @Override
    public Page<Brand> getBrandData(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return brandRepository.findAll(pageable);
    }

    @Override
    public List<Brand> getBrandData() {
        return brandRepository.findAll();
    }

    @Override
    public List<Brand> getBrandDataByCategory(Long categoryId) {
        return brandRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public Brand updateBrand(Long id, BrandRequest brandRequest) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        if (brandRequest.getCategoryId() != null) {
            brand.setCategory(
                    categoryRepository.findById(brandRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        return brandRepository.save(brandRequest.toBrand(brand));
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
    }
}
