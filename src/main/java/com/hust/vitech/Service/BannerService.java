package com.hust.vitech.Service;

import com.hust.vitech.Model.Banner;
import com.hust.vitech.Request.BannerRequest;
import org.springframework.data.domain.Page;

public interface BannerService {
    Banner createNewBanner(BannerRequest bannerRequest);

    Banner updateBanner(Long id, BannerRequest bannerRequest);

    void deleteBanner(Long id);

    Banner getBannerById(Long id);

    Page<Banner> getAllBanner(int size, int page, String sortBy);
}
