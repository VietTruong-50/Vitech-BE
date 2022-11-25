package com.hust.vitech.Controller;

import com.hust.vitech.Model.Category;
import com.hust.vitech.Request.CategoryRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "/categories", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Category> createNewCategory(@RequestPart("category") CategoryRequest categoryRequest,
                                                   @RequestPart("category_image") MultipartFile categoryImage) {
        try {
            categoryRequest.setCategoryImageByte(categoryImage.getBytes());
            Category category = categoryService.createNewCategory(categoryRequest);
            return ApiResponse.successWithResult(category);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request", null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/categories/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Category> updateCategory(@PathVariable("id") Long id, @Valid @RequestPart("category") CategoryRequest categoryRequest,
                                                @RequestPart("category_image") MultipartFile categoryImage) {
        try {
            categoryRequest.setCategoryImageByte(categoryImage.getBytes());
            Category category = categoryService.updateCategory(id, categoryRequest);
            return ApiResponse.successWithResult(category);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request", null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/categories/{id}", produces = "application/json")
    public ApiResponse<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.successWithResult("Delete success");
    }

    @GetMapping(value = "/categories", produces = "application/json")
    public ApiResponse<Page<Category>> getAllCategory(@RequestParam int size,
                                                      @RequestParam int page,
                                                      @RequestParam String sortBy) {
        return ApiResponse.successWithResult(categoryService.getAllCategory(size, page, sortBy));
    }

    @GetMapping(value = "categories/{id}", produces = "application/json")
    public ApiResponse<Category> getCategoryById(@PathVariable("id") Long id) {
        return ApiResponse.successWithResult(categoryService.getCategoryById(id));
    }
}
