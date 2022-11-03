package com.hust.vitech.Controller;

import com.hust.vitech.Model.Banner;
import com.hust.vitech.Request.BannerRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.Impl.BannerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api")
public class BannerController {

    @Autowired
    private BannerServiceImpl bannerService;

    @PostMapping(value = {"/banners"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Banner> createNewBanner(@RequestPart("banner") BannerRequest bannerRequest,
                                               @RequestPart("image") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            bannerRequest.setImageName(fileName);
            Banner banner = bannerService.createNewBanner(bannerRequest);

            String uploadDir = "/banners/" + banner.getId();
            FileUploadUtil.saveFile(uploadDir, file.getOriginalFilename(), file);

            return ApiResponse.successWithResult(banner);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = {"/banners/{id}"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Banner> updateBanner(@PathVariable("id") Long id,
                                          @RequestPart("banner") BannerRequest bannerRequest,
                                          @RequestPart("image") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            bannerRequest.setImageName(fileName);
            Banner banner = bannerService.updateBanner(id, bannerRequest);

            String uploadDir = "/banners/" + banner.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, file);

            return ApiResponse.successWithResult(banner);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = {"/banners/{id}"}, produces = "application/json")
    public ApiResponse<?> deleteBanner(@PathVariable("id") Long id) {
        bannerService.deleteBanner(id);
        return ApiResponse.successWithResult(null, "Delete success");
    }

    @GetMapping(value = "/banners/{id}", produces = "application/json")
    public ApiResponse<Banner> getBannerById(@PathVariable("id")Long id) {
        return ApiResponse.successWithResult(bannerService.getBannerById(id));
    }

    @GetMapping(value = "/banners", produces = "application/json")
    public ApiResponse<Page<Banner>> getAllBanner(@RequestParam int size,
                                                  @RequestParam int page,
                                                  @RequestParam String sortBy){
        return ApiResponse.successWithResult(bannerService.getAllBanner(size, page, sortBy));
    }
}
