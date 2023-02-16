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
    @Query("SELECT p FROM Product p JOIN SubCategory b ON b.id = p.subCategory.id " +
            "JOIN Category c ON c.id = b.category.id " +
            "WHERE c.name = ?1")
    Page<Product> findAllByCategoryName(Pageable pageable, String categoryName);

    //Tim tat ca sp cung hang
    Page<Product> findAllBySubCategory_SubCateName(Pageable pageable, String cateName);

    //Xem chi tiet san pham
    Page<Product> findAllByProductCodeContaining(Pageable pageable, String code);

    //Cac san pham lien quan
    List<Product> findTop10BySubCategory_SubCateNameContaining(String subCateName);

}



