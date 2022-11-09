package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Banner;
import com.hust.vitech.Repository.BannerRepository;
import com.hust.vitech.Request.BannerRequest;
import com.hust.vitech.Service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public Banner createNewBanner(BannerRequest bannerRequest) {
        if (!bannerRepository.existsByName(bannerRequest.getName())) {
            Banner banner = new Banner();
            return bannerRepository.save(bannerRequest.toBanner(banner));
        }
        return null;
    }

    @Override
    public Banner updateBanner(Long id, BannerRequest bannerRequest) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner not found"));
        return bannerRepository.save(bannerRequest.toBanner(banner));
    }

    @Override
    public void deleteBanner(Long id) {
        bannerRepository.findById(id).map(
                banner -> {
                    bannerRepository.delete(banner);
                    return ResponseEntity.ok().build();
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Banner not found"));
    }

    @Override
    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Banner> getAllBanner(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bannerRepository.findAll(pageable);
    }
}
