package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.ImageModel;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Repository.BrandRepository;
import com.hust.vitech.Repository.CategoryRepository;
import com.hust.vitech.Repository.ProductRepository;
import com.hust.vitech.Request.ProductRequest;
import com.hust.vitech.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Product createNewProduct(ProductRequest productRequest) {
        Product product = new Product();

        if (productRequest.getCategory_id() != null) {
            product.setCategory(
                    categoryRepository.findById(productRequest.getCategory_id())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        if (productRequest.getBrand_id() != null) {
            product.setBrand(
                    brandRepository.findById(productRequest.getBrand_id())
                            .orElseThrow(() -> new ResourceNotFoundException("Brand not found")));
        }

        return productRepository.save(productRequest.toProduct(product));

    }

    @Override
    public Product updateProduct(ProductRequest productRequest, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product != null) {

            if (productRequest.getCategory_id() != null) {
                product.setCategory(
                        categoryRepository.findById(productRequest.getCategory_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
            }

            if (productRequest.getBrand_id() != null) {
                product.setBrand(
                        brandRepository.findById(productRequest.getBrand_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Brand not found")));
            }

            return productRepository.save(productRequest.toProduct(product));
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).map(
                product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().build();
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Page<Product> findAll(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAll(pageable);
    }

    public Set<ImageModel> uploadImages(MultipartFile[] file) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile f : file) {
            ImageModel imageModel = new ImageModel(
                    f.getOriginalFilename(),
                    f.getContentType(),
                    f.getBytes()
            );

            imageModels.add(imageModel);
        }

        return imageModels;
    }

    @Override
    public List<Product> findProductsByBrandName(String brandName) {
        return productRepository.findTop4ByBrand_BrandName(brandName);
    }

    @Override
    public List<Product> findProductsByCategoryName(String categoryName) {
        return productRepository.findTop4ByCategory_Name(categoryName);
    }

    @Override
    public Page<Product> findAllByCategoryName(String categoryName, int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAllByCategoryName(pageable, categoryName);
    }

    @Override
    public Page<Product> findAllByBrandName(String brandName, int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAllByBrandBrandName(pageable, brandName);
    }
}
