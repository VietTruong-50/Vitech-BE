package com.hust.vitech.Service;

import com.hust.vitech.Model.Brand;
import com.hust.vitech.Request.BrandRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    Brand createNewBrand(BrandRequest brandRequest);
    Page<Brand> getBrandData(int size, int page, String sortBy);
    List<Brand> getBrandData();
    List<Brand> getBrandDataByCategory(Long categoryId);
    Brand updateBrand(Long id, BrandRequest brandRequest);
    Brand getBrandById(Long id);
}
