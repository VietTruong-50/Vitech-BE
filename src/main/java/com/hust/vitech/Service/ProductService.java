package com.hust.vitech.Service;

import com.hust.vitech.Model.ImageModel;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ProductService {
    Product createNewProduct(ProductRequest productRequest);

    Product updateProduct(ProductRequest productRequest, Long productId);

    ResponseEntity<?> deleteProduct(Long id) ;

    Product findProductById(Long id);

    Page<Product> findAll(int size, int page, String sortBy);

    Set<ImageModel> uploadImage(MultipartFile[] files);
}
