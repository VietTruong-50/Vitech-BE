package com.hust.vitech.Repository;

import com.hust.vitech.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    List<Product> findTop4ByBrand_BrandName(String brandName);

    List<Product> findTop4ByCategory_Name(String categoryName);

    Page<Product> findAllByCategoryName(Pageable pageable, String categoryName);

    Page<Product> findAllByBrandBrandName(Pageable pageable, String brandName);
}
