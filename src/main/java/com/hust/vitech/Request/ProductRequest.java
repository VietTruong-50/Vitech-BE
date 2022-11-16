package com.hust.vitech.Request;

import com.hust.vitech.Model.ImageModel;
import com.hust.vitech.Model.Product;
import lombok.Data;

import java.util.Set;

@Data
public class ProductRequest {

    private String name;

    private String content;

    private Double actualPrice;

    private int quantity;

    private Set<ImageModel> productImages;

    private String featureImageName;

    private byte[] featureImageByte;

    private Long category_id;

    private Long brand_id;

    public Product toProduct(Product product){
        product.setName(this.getName());
        product.setContent(this.getContent());
        product.setFeatureImageName(this.getFeatureImageName());
        product.setActualPrice(this.getActualPrice());
        product.setProductImages(this.getProductImages());
        product.setQuantity(this.getQuantity());
        product.setFeatureImageByte(this.getFeatureImageByte());

        return product;
    }
}
