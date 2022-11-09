package com.hust.vitech.Request;

import com.hust.vitech.Model.Brand;
import lombok.Data;

@Data
public class BrandRequest {
    private String brandName;

    private String description;

    public Brand toBrand(Brand brand){
        brand.setBrandName(this.getBrandName());
        brand.setDescription(this.getDescription());
        return brand;
    }
}
