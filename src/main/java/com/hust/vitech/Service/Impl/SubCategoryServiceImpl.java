package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Category;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Model.SubCategory;
import com.hust.vitech.Repository.SubCategoryRepository;
import com.hust.vitech.Repository.CategoryRepository;
import com.hust.vitech.Request.SubCategoryRequest;
import com.hust.vitech.Service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public SubCategory createNewSubCategory(SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = new SubCategory();

        if (subCategoryRequest.getCategoryId() != null) {
            subCategory.setCategory(
                    categoryRepository.findById(subCategoryRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        return subCategoryRepository.save(subCategoryRequest.toSubCategory(subCategory));
    }

    @Override
    public Page<SubCategory> getSubCategoryData(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return subCategoryRepository.findAll(pageable);
    }

    @Override
    public List<SubCategory> getSubCategoryData() {
        return subCategoryRepository.findAll();
    }

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<SubCategory> getSubCategoryDataByCategory(List<String> names) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SubCategory> criteriaQuery = criteriaBuilder.createQuery(SubCategory.class);

        Root<SubCategory> root = criteriaQuery.from(SubCategory.class);
        Join<SubCategory, Category> sbc_ct = root.join("category");

        List<Predicate> conditions = new ArrayList<>();

        TypedQuery<SubCategory> typedQuery ;

        if (names != null) {
            for (String name : names) {
                conditions.add(criteriaBuilder.equal(sbc_ct.get("name"), name));
            }

            typedQuery = entityManager.createQuery(criteriaQuery
                    .select(root)
                    .where(criteriaBuilder.or(conditions.toArray(new Predicate[]{})))
            );
        } else {
            typedQuery = entityManager.createQuery(criteriaQuery
                    .select(root));
        }

        return typedQuery.getResultList();
    }

    @Override
    public SubCategory updateSubCategory(Long id, SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        if (subCategoryRequest.getCategoryId() != null) {
            subCategory.setCategory(
                    categoryRepository.findById(subCategoryRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        return subCategoryRepository.save(subCategoryRequest.toSubCategory(subCategory));
    }

    @Override
    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));
    }
}
