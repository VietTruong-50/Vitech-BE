package com.hust.vitech.Service;

import com.hust.vitech.Model.Slider;
import com.hust.vitech.Request.SliderRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface SliderService {
    Slider createNewSlider(SliderRequest sliderRequest);
    Slider updateSlider(Long id, SliderRequest sliderRequest);
    void deleteSlider(Long id);
    Page<Slider> getAllSlider(int size, int page, String sortBy);
    Slider getSliderById(Long id);
}
