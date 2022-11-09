package com.hust.vitech.Service;

import com.hust.vitech.Model.Brand;
import com.hust.vitech.Request.BrandRequest;
import org.springframework.data.domain.Page;

public interface BrandService {
    Brand createNewBrand(BrandRequest brandRequest);
    Page<Brand> getAllBrands(int size, int page, String sortBy);
    Brand updateBrand(Long id, BrandRequest brandRequest);
    Brand getBrandById(Long id);
}
