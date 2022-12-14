package com.hust.vitech.Service;

import com.hust.vitech.Model.Category;
import com.hust.vitech.Request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    Category createNewCategory(CategoryRequest categoryRequest);
    Category updateCategory(Long id, CategoryRequest categoryRequest);
    ResponseEntity<?> deleteCategory(Long id) ;
    Category getCategoryById(Long id);
    Page<Category> getAllCategory(int size, int page, String sortBy);
}
