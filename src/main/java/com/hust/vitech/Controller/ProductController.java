package com.hust.vitech.Controller;

import com.hust.vitech.Model.Product;
import com.hust.vitech.Request.ProductRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping(value = {"/products"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Product> createNewProduct(@RequestPart("product") ProductRequest productRequest,
                                                @RequestPart("feature_image") MultipartFile featureImage,
                                                @RequestPart("imageFiles") MultipartFile[] files
    ) {
        try {
            productRequest.setProductImages(productService.uploadImage(files));
            productRequest.setFeatureImageName(featureImage.getOriginalFilename());
            Product product = productService.createNewProduct(productRequest);

            String uploadDir = "/products/" + product.getId() + "/feature_img";
            FileUploadUtil.saveFile(uploadDir, featureImage.getOriginalFilename(),featureImage);

            uploadDir = "/products/" + product.getId() + "/details";
            FileUploadUtil.saveFiles(uploadDir, files);

            return ApiResponse.successWithResult(product);
        } catch (IOException e) {
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = {"/products/{id}"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Product> updateProduct(@PathVariable("id") Long productId,
                                              @RequestPart("featureImage") MultipartFile featureImage,
                                           @RequestPart("product") ProductRequest productRequest,
                                           @RequestPart("imageFile") MultipartFile[] files){
        try {
            productRequest.setProductImages(productService.uploadImage(files));
            productRequest.setFeatureImageName(featureImage.getOriginalFilename());
            Product product = productService.updateProduct(productRequest, productId);

            String uploadDir = "/products/" + product.getId() + "/feature_img";
            FileUploadUtil.saveFile(uploadDir, featureImage.getOriginalFilename(),featureImage);

            uploadDir = "/products/" + product.getId() + "/details";
            FileUploadUtil.saveFiles(uploadDir, files);

            return ApiResponse.successWithResult(product);
        } catch (IOException e) {
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/products/{id}"}, produces = "application/json")
    public ApiResponse<Product> findProductById(@PathVariable("id") Long productId){
        try {
            return ApiResponse.successWithResult(productService.findProductById(productId));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.failureWithCode("", "Not found",null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = {"/products"}, produces = "application/json")
    public ApiResponse<Page<Product>> getAllProduct(@RequestParam int size,
                                                    @RequestParam int page,
                                                    @RequestParam String sortBy){
        try {
            return ApiResponse.successWithResult(productService.findAll(size, page, sortBy));
        } catch (Exception e) {
            return ApiResponse.failureWithCode("", "Not found",null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/products/{id}", produces = "application/json")
    public ApiResponse<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ApiResponse.successWithResult(null , "Delete success");
    }

}
