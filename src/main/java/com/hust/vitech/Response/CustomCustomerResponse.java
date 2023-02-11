package com.hust.vitech.Response;

import com.hust.vitech.Model.Address;
import com.hust.vitech.Model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomCustomerResponse {
    private Customer customer;

    private List<Address> addresses;
}
