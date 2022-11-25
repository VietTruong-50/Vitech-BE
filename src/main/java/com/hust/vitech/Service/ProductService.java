package com.hust.vitech.Service;

import com.hust.vitech.Model.ImageModel;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {
    Product createNewProduct(ProductRequest productRequest);

    Product updateProduct(ProductRequest productRequest, Long productId);

    void deleteProduct(Long id) ;

    Product findProductById(Long id);

    Page<Product> findAll(int size, int page, String sortBy);

    Set<ImageModel> uploadImages(MultipartFile[] files) throws IOException;

    List<Product> findProductsByBrandName(String brandName);
    List<Product> findProductsByCategoryName(String categoryName);

    Page<Product> findAllByCategoryName(String categoryName, int size, int page, String sortBy);

    Page<Product> findAllByBrandName(String brandName, int size, int page, String sortBy);
}
