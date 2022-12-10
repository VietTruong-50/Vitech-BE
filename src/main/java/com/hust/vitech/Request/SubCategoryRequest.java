package com.hust.vitech.Request;

import com.hust.vitech.Model.SubCategory;
import lombok.Data;

@Data
public class SubCategoryRequest {
    private String subCateName;

    private String description;

    private Long categoryId;

    public SubCategory toSubCategory(SubCategory subCategory){
        subCategory.setSubCateName(this.getSubCateName());
        subCategory.setDescription(this.getDescription());
        return subCategory;
    }
}
