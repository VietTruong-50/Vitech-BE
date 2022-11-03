package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.ImageModel;
import com.hust.vitech.Model.Product;
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

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createNewProduct(ProductRequest productRequest) {

        if (productRepository.existsByName(productRequest.getName())) {
            Product product = new Product();

            if (productRequest.getCategory_id() != null) {
                product.setCategory(
                        categoryRepository.findById(productRequest.getCategory_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
            }

            return productRepository.save(productRequest.toProduct(product));
        }
        return null;
    }

    @Override
    public Product updateProduct(ProductRequest productRequest, Long productId) {
        Product product = productRepository.findProductById(productId).orElse(null);

        if (product != null) {

            if (productRequest.getCategory_id() != null) {
                product.setCategory(
                        categoryRepository.findById(productRequest.getCategory_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
            }

            return productRepository.save(productRequest.toProduct(product));
        }
        return null;
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        return productRepository.findById(id).map(
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
        Pageable pageable = PageRequest.of(size, page, Sort.by(sortBy));
        return productRepository.findAll(pageable);
    }

    @Override
    public Set<ImageModel> uploadImage(MultipartFile[] file) {
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile f : file) {
            ImageModel imageModel = new ImageModel(
                    f.getOriginalFilename(),
                    f.getContentType()
            );

            imageModels.add(imageModel);
        }

        return imageModels;
    }
}
