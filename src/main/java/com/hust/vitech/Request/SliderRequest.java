package com.hust.vitech.Request;

import com.hust.vitech.Model.Slider;
import lombok.Data;

@Data
public class SliderRequest {
    private String name;

    private String description;

    private String imageName;

    public Slider toSlider(Slider slider){
        slider.setName(this.getName());
        slider.setDescription(this.getDescription());
        slider.setImageName(this.getImageName());

        return slider;
    }
}
