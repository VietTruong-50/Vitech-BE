package com.hust.vitech.Repository;

import com.hust.vitech.Model.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    boolean existsByName(String name);
    Page<Banner> findAll(Pageable pageable);
}
