package com.hust.vitech.Controller;

import com.hust.vitech.Model.SubCategory;
import com.hust.vitech.Request.SubCategoryRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping(value = "/subCategories")
    public ApiResponse<SubCategory> createNewSubCategory(@RequestBody SubCategoryRequest subCategoryRequest) {
        return ApiResponse.successWithResult(subCategoryService.createNewSubCategory(subCategoryRequest));
    }

    @GetMapping(value = "subCategories", produces = "application/json")
    public ApiResponse<Page<SubCategory>> getAllSubCategory(@RequestParam int size,
                                                      @RequestParam int page,
                                                      @RequestParam String sortBy) {
        return ApiResponse.successWithResult(subCategoryService.getSubCategoryData(size, page, sortBy));
    }

    @GetMapping(value = "categories/{name}/SubCategories", produces = "application/json")
    public ApiResponse<List<SubCategory>> getSubCategoryDataByCategory(@PathVariable("name") String name) {
        return ApiResponse.successWithResult(subCategoryService.getSubCategoryDataByCategory(name));
    }

    @GetMapping(value = "subCategories/{id}", produces = "application/json")
    public ApiResponse<SubCategory> getSubCategoryById(@PathVariable("id") Long id) {
        return ApiResponse.successWithResult(subCategoryService.getSubCategoryById(id));
    }

    @PutMapping(value = "subCategories/{id}", produces = "application/json")
    public ApiResponse<SubCategory> updateSubCategory(@PathVariable("id") Long id, @RequestBody SubCategoryRequest subCategoryRequest) {
        return ApiResponse.successWithResult(subCategoryService.updateSubCategory(id, subCategoryRequest));
    }
}
