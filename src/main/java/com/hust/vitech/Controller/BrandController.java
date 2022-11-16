package com.hust.vitech.Controller;

import com.hust.vitech.Model.Brand;
import com.hust.vitech.Request.BrandRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping(value = "/brands")
    public ApiResponse<Brand> createNewBrand(@RequestBody BrandRequest brandRequest) {
        return ApiResponse.successWithResult(brandService.createNewBrand(brandRequest));
    }

    @GetMapping(value = "brands", produces = "application/json")
    public ApiResponse<Page<Brand>> getAllBrand(@RequestParam int size,
                                                @RequestParam int page,
                                                @RequestParam String sortBy) {
        return ApiResponse.successWithResult(brandService.getBrandData(size, page, sortBy));
    }

    @GetMapping(value = "brands/all", produces = "application/json")
    public ApiResponse<List<Brand>> getBrandData() {
        return ApiResponse.successWithResult(brandService.getBrandData());
    }

    @GetMapping(value = "brands/{id}", produces = "application/json")
    public ApiResponse<Brand> getBrandById(@PathVariable("id") Long id) {
        return ApiResponse.successWithResult(brandService.getBrandById(id));
    }

    @PutMapping(value = "brands/{id}", produces = "application/json")
    public ApiResponse<Brand> updateBrand(@PathVariable("id") Long id, @RequestBody BrandRequest brandRequest) {
        return ApiResponse.successWithResult(brandService.updateBrand(id, brandRequest));
    }
}
