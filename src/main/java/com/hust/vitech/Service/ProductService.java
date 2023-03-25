package com.hust.vitech.Service;

import com.hust.vitech.Exception.CustomException;
import com.hust.vitech.Model.ImageModel;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {
    Product createNewProduct(ProductRequest productRequest) throws CustomException;

    Product updateProduct(ProductRequest productRequest, Long productId) throws CustomException;

    void deleteProduct(Long id) throws CustomException;

    Product findProductById(Long id) throws CustomException;

    Page<Product> findAll(int size, int page, String sortBy);

    Set<ImageModel> uploadImages(MultipartFile[] files) throws IOException;

    List<Product> findProductsByBrandName(String brandName);

    List<Product> findTop10ProductsBySubCateName(String brandName);

    Page<Product> findProductsByCategoryName(String categoryName, int size, int page, String sortBy);

    Page<Product> findAllBySubCategoryName(String brandName, int size, int page, String sortBy);

    Page<Product> filterProduct(List<String> categories,
                                List<String> subCategories,
                                int firstPrice,
                                int secondPrice, int page,
                                int size, String sortBy, String searchText);

    Page<Product> findAllByProductCodeContaining(String productCode, int page, int size, String sortBy);
}
