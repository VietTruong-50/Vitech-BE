package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Slider;
import com.hust.vitech.Repository.SliderRepository;
import com.hust.vitech.Request.SliderRequest;
import com.hust.vitech.Service.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SliderServiceImpl implements SliderService {
    @Autowired
    private SliderRepository sliderRepository;


    @Override
    public Slider createNewSlider(SliderRequest sliderRequest) {
        if(!sliderRepository.existsByName(sliderRequest.getName())){
            Slider slider = new Slider();
            return sliderRepository.save(sliderRequest.toSlider(slider));
        }
        return null;
    }

    @Override
    public Slider updateSlider(Long id, SliderRequest sliderRequest) {
        Slider slider = sliderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slider not found"));
        return sliderRepository.save(sliderRequest.toSlider(slider));
    }

    @Override
    public void deleteSlider(Long id) {
        Slider slider = sliderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slider not found"));
        sliderRepository.delete(slider);
    }

    @Override
    public Page<Slider> getAllSlider(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return sliderRepository.findAll(pageable);
    }

    @Override
    public Slider getSliderById(Long id) {
        return sliderRepository.findById(id).orElse(null);
    }


}
