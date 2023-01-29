package com.hust.vitech.Request;

import lombok.Data;

@Data
public class FilterRequest {
    Long categoryId;
    Long subCateId;
    int firstPrice;
    int secondPrice;
    int page;
    int size;
    String sortBy;
}
