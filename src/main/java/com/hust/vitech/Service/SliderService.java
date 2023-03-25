package com.hust.vitech.Service;

import com.hust.vitech.Exception.CustomException;
import com.hust.vitech.Model.Slider;
import com.hust.vitech.Request.SliderRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface SliderService {
    Slider createNewSlider(SliderRequest sliderRequest);
    Slider updateSlider(Long id, SliderRequest sliderRequest) throws CustomException;
    void deleteSlider(Long id) throws CustomException;
    Page<Slider> getAllSlider(int size, int page, String sortBy);
    Slider getSliderById(Long id);
}
