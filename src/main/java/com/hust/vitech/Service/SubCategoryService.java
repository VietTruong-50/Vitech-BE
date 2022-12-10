package com.hust.vitech.Service;

import com.hust.vitech.Model.SubCategory;
import com.hust.vitech.Request.SubCategoryRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubCategoryService {
    SubCategory createNewSubCategory(SubCategoryRequest subCategoryRequest);
    Page<SubCategory> getSubCategoryData(int size, int page, String sortBy);
    List<SubCategory> getSubCategoryData();
    List<SubCategory> getSubCategoryDataByCategory(String name);
    SubCategory updateSubCategory(Long id, SubCategoryRequest subCategoryRequest);
    SubCategory getSubCategoryById(Long id);
}
