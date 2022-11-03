package com.hust.vitech.Repository;

import com.hust.vitech.Model.Slider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Long> {
    Page<Slider> findAll(Pageable pageable);
    boolean existsByName(String name);
}
