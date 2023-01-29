package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Category;
import com.hust.vitech.Model.ImageModel;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Model.SubCategory;
import com.hust.vitech.Repository.SubCategoryRepository;
import com.hust.vitech.Repository.CategoryRepository;
import com.hust.vitech.Repository.ProductRepository;
import com.hust.vitech.Request.ProductRequest;
import com.hust.vitech.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public Product createNewProduct(ProductRequest productRequest) {
        Product product = new Product();

//        if (productRequest.getCategory_id() != null) {
//            product.setCategory(
//                    categoryRepository.findById(productRequest.getCategory_id())
//                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
//        }

        if (productRequest.getBrand_id() != null) {
            product.setSubCategory(
                    subCategoryRepository.findById(productRequest.getBrand_id())
                            .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found")));
        }

        return productRepository.save(productRequest.toProduct(product));

    }

    @Override
    public Product updateProduct(ProductRequest productRequest, Long productId) {
        Product product = this.findProductById(productId);

        if (product != null) {

//            if (productRequest.getCategory_id() != null) {
//                product.setCategory(
//                        categoryRepository.findById(productRequest.getCategory_id())
//                                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
//            }

            if (productRequest.getBrand_id() != null) {
                product.setSubCategory(
                        subCategoryRepository.findById(productRequest.getBrand_id())
                                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found")));
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
//        return productRepository.findTop4ByBrand_BrandName(brandName);
        return null;
    }

    @Override
    public Page<Product> findProductsByCategoryName(String categoryName, int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAllByCategoryName(pageable, categoryName);
    }

    @Override
    public Page<Product> findAllBySubCategoryName(String brandName, int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAllBySubCategory_SubCateName(pageable, brandName);
    }

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<Product> filterProduct(List<String> categories,
                                       List<String> subCategories,
                                       int firstPrice,
                                       int secondPrice,
                                       int page, int size,
                                       String sortBy, String searchText) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        Join<Product, SubCategory> prd_sbc = root.join("subCategory");
        Join<SubCategory, Category> sbc_ct = prd_sbc.join("category");
        List<Predicate> conditions = new ArrayList<>();

        if (subCategories != null) {
            List<Predicate> predicatesNames = new ArrayList<>();

            for (String name : subCategories) {
                predicatesNames.add(criteriaBuilder.equal(prd_sbc.get("subCateName"), name));
            }

            conditions.add(criteriaBuilder.or(predicatesNames.toArray(new Predicate[]{})));
        }

        conditions.add(criteriaBuilder.between(root.get("actualPrice"), firstPrice, secondPrice));

        if (categories != null) {
            List<Predicate> predicatesIds = new ArrayList<>();

            for (String name : categories) {
                predicatesIds.add(criteriaBuilder.equal(sbc_ct.get("name"), name));
            }

            conditions.add(criteriaBuilder.or(predicatesIds.toArray(new Predicate[]{})));
        }

        if (!Objects.equals(searchText, "")) {
            conditions.add(criteriaBuilder.like(root.get("name").as(String.class), '%' + searchText + '%'));
        }

        TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery
                .select(root)
                .where(criteriaBuilder.and(conditions.toArray(new Predicate[]{})))
        );

        List<Product> result = typedQuery.getResultList();

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return new PageImpl<>(result, pageable, result.size());
    }
}
