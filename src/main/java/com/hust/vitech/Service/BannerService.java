package com.hust.vitech.Service;

import com.hust.vitech.Model.Banner;
import com.hust.vitech.Request.BannerRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BannerService {
    Banner createNewBanner(BannerRequest bannerRequest);

    Banner updateBanner(Long id, BannerRequest bannerRequest);

    ResponseEntity<?> deleteBanner(Long id);

    Banner getBannerById(Long id);

    Page<Banner> getAllBanner(int size, int page, String sortBy);
}
