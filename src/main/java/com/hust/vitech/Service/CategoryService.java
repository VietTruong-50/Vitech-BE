package com.hust.vitech.Service;

import com.hust.vitech.Exception.ApiException;
import com.hust.vitech.Exception.CustomException;
import com.hust.vitech.Model.Category;
import com.hust.vitech.Request.CategoryRequest;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Category createNewCategory(CategoryRequest categoryRequest) ;
    Category updateCategory(Long id, CategoryRequest categoryRequest) throws CustomException;
    void deleteCategory(Long id) ;
    Category getCategoryById(Long id) throws CustomException;
    Page<Category> getAllCategory(int size, int page, String sortBy);
}
