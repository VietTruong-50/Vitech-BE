package com.hust.vitech.Controller;

import com.hust.vitech.Model.Category;
import com.hust.vitech.Model.Slider;
import com.hust.vitech.Request.CategoryRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "/categories", produces = "application/json")
    public ApiResponse<Category> createNewCategory(@RequestBody CategoryRequest categoryRequest){
        try{
            Category category = categoryService.createNewCategory(categoryRequest);
            return ApiResponse.successWithResult(category);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/categories/{id}", produces = "application/json")
    public ApiResponse<Category> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequest categoryRequest){
        try{
            Category category = categoryService.updateCategory(id, categoryRequest);
            return ApiResponse.successWithResult(category);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/categories/{id}", produces = "application/json")
    public ApiResponse<?> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ApiResponse.successWithResult("Delete success");
    }

    @GetMapping(value = "/categories", produces = "application/json")
    public ApiResponse<List<Category>> getAllCategory(){
        return ApiResponse.successWithResult(categoryService.getAllCategory());
    }
}
