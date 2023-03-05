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

    //Loc san pham cung phan loai
    @Query(value = "SELECT p.* FROM products p JOIN categories c ON c.id = p.cate_id WHERE c.name = ?1", nativeQuery = true)
    List<Product> findAllByCategoryName(String categoryName);

    //Tim tat ca sp cung hang
    Page<Product> findAllBySubCategory_SubCateName(Pageable pageable, String cateName);

    //Xem chi tiet san pham
    Page<Product> findAllByProductCodeContaining(Pageable pageable, String code);

    //Cac san pham lien quan
    List<Product> findTop10BySubCategory_SubCateNameContaining(String subCateName);

}



