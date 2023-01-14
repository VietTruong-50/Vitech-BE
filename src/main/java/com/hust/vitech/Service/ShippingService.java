package com.hust.vitech.Service;

import com.hust.vitech.Model.ShippingMethod;
import com.hust.vitech.Request.ShippingMethodRequest;
import org.springframework.data.domain.Page;

public interface ShippingService {
    Page<ShippingMethod> findAll(int page, int size, String sortBy);
    ShippingMethod createNewShippingMethod(ShippingMethodRequest shippingMethodRequest);
    ShippingMethod updateShippingMethod(Long methodId, ShippingMethodRequest shippingMethodRequest);
    void deleteMethod(Long methodId);
}
