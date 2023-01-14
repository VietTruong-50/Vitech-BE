package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.ShippingMethod;
import com.hust.vitech.Repository.ShippingMethodRepository;
import com.hust.vitech.Request.ShippingMethodRequest;
import com.hust.vitech.Service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @Override
    public Page<ShippingMethod> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return shippingMethodRepository.findAll(pageable);
    }

    @Override
    public ShippingMethod createNewShippingMethod(ShippingMethodRequest shippingMethodRequest) {
        ShippingMethod shippingMethod = new ShippingMethod();

        shippingMethod.setShippingMethod(shippingMethodRequest.getShippingMethod());
        shippingMethod.setPrice(shippingMethod.getPrice());

        return shippingMethodRepository.save(shippingMethod);
    }

    @Override
    public ShippingMethod updateShippingMethod(Long methodId, ShippingMethodRequest shippingMethodRequest) {
        Optional<ShippingMethod> shippingMethod = shippingMethodRepository.findById(methodId);

        if (shippingMethod.isPresent()) {
            shippingMethod.get().setShippingMethod(shippingMethodRequest.getShippingMethod());
            shippingMethod.get().setPrice(shippingMethodRequest.getPrice());

            return shippingMethodRepository.save(shippingMethod.get());
        }
        return null;
    }

    @Override
    public void deleteMethod(Long methodId) {
        shippingMethodRepository.findById(methodId).map(method -> {
                    shippingMethodRepository.delete(method);
                    return ResponseEntity.ok().build();
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Shipping method not found"));
    }
}
