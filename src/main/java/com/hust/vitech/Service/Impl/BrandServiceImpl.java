package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Brand;
import com.hust.vitech.Repository.BrandRepository;
import com.hust.vitech.Request.BrandRequest;
import com.hust.vitech.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand createNewBrand(BrandRequest brandRequest) {
        Brand brand = new Brand();
        return brandRepository.save(brandRequest.toBrand(brand));
    }

    @Override
    public Page<Brand> getAllBrands(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand updateBrand(Long id, BrandRequest brandRequest) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        return brandRepository.save(brandRequest.toBrand(brand));
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
    }
}
