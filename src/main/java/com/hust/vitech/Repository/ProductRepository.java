package com.hust.vitech.Repository;

import com.hust.vitech.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

//    List<Product> findTop4ByBrand_BrandName(String brandName);

//    List<Product> findTop4ByCategory_Name(String categoryName);

    //Loc san pham cung phan loai
    @Query("SELECT p FROM Product p JOIN Brand b ON b.id = p.brand.id " +
            "JOIN Category c ON c.id = b.category.id " +
            "WHERE c.name = ?1")
    Page<Product> findAllByCategoryName(Pageable pageable, String categoryName);

    //Tim san pham
    @Query(value = "SELECT * FROM products p " +
            "JOIN brands b ON b.id = p.brand_id " +
            "JOIN categories c ON c.id = b.category_id " +
            "WHERE (c.name REGEXP ?1) AND (p.name REGEXP ?2 " +
            "OR p.product_code REGEXP ?2 " +
            "OR b.brand_name REGEXP ?2)", nativeQuery = true)
    Page<Product> searchProduct(Pageable pageable, Long categoryId, String textSearch);

    //Tim tat ca sp cung hang
    Page<Product> findAllByBrandBrandName(Pageable pageable, String brandName);

    //Xem chi tiet san pham
    Product findProductByProductCode(String code);

    //Cac san pham lien quan
    List<Product> findTop5ByBrand_BrandName(String brandName);

    @Query("SELECT p FROM Product p WHERE p.actualPrice BETWEEN ?1 AND ?2")
    Page<Product> findAllByActualPriceBetween2Values(Pageable pageable, Double firstPrice, Double secondPrice);
}
