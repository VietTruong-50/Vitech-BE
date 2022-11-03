package com.hust.vitech.Request;

import com.hust.vitech.Model.Banner;
import lombok.Data;

@Data
public class BannerRequest {
    private String name;

    private String content;

    private String imageName;

    public Banner toBanner(Banner banner){
        banner.setName(this.getName());
        banner.setContent(this.getContent());
        banner.setImageName(this.getImageName());

        return banner;
    }
}
