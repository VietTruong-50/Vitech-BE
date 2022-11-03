package com.hust.vitech.Controller;

import com.hust.vitech.Model.Slider;
import com.hust.vitech.Request.SliderRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.Impl.SliderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api")
public class SliderController {

    @Autowired
    private SliderServiceImpl sliderService;

    @PostMapping(value = "/sliders", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Slider> createNewSlider(@RequestPart("slider") SliderRequest sliderRequest,
                                               @RequestPart("image") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            sliderRequest.setImageName(fileName);
            Slider slider = sliderService.createNewSlider(sliderRequest);

            String uploadDir =  "/sliders/" + slider.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, file);

            return ApiResponse.successWithResult(slider);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/sliders/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ApiResponse<Slider> updateSlider(@PathVariable("id") Long id,
                                          @RequestPart("slider") SliderRequest sliderRequest,
                                          @RequestPart("image") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            sliderRequest.setImageName(fileName);
            Slider slider = sliderService.updateSlider(id, sliderRequest);

            String uploadDir =  "/sliders/" + slider.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, file);

            return ApiResponse.successWithResult(slider);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.failureWithCode("", "Bad request",null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/sliders/{id}", produces = "application/json")
    public ApiResponse<?> deleteSlider(@PathVariable("id") Long id){
        sliderService.deleteSlider(id);
        return ApiResponse.successWithResult(null , "Delete success");
    }

    @GetMapping(value = "/sliders", produces = "application/json")
    public ApiResponse<Page<Slider>> getAllSlider(@RequestParam int size,
                                                  @RequestParam int page,
                                                  @RequestParam String sortBy){
        return ApiResponse.successWithResult(sliderService.getAllSlider(size, page, sortBy));
    }

    @GetMapping(value = "/slider/{id}", produces = "application/json")
    public ApiResponse<Slider> getSliderById(@PathVariable("id") Long id){
        return ApiResponse.successWithResult(sliderService.getSliderById(id));
    }
}
