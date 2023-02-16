package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Address;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Notification;
import com.hust.vitech.Repository.AddressRepository;
import com.hust.vitech.Repository.CustomerRepository;
import com.hust.vitech.Repository.NotificationRepository;
import com.hust.vitech.Request.AddressRequest;
import com.hust.vitech.Request.CustomerRequest;
import com.hust.vitech.Response.CustomCustomerResponse;
import com.hust.vitech.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Notification> getAllNotificationsByCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Customer customer = customerRepository.findCustomerByUserName(authentication.getName()).get();

        return notificationRepository.findAllByCustomer(customer);
    }

    @Override
    public Address createNewAddress(AddressRequest addressRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Customer> customer = customerRepository.findCustomerByUserName(authentication.getName());

        if (customer.isPresent()) {
            Address address = new Address();

            address.setReceiverName(addressRequest.getReceiverName());
            address.setPhone(addressRequest.getPhone());
            address.setEmail(addressRequest.getEmail());
            address.setCity(addressRequest.getCity());
            address.setSubDistrict(addressRequest.getSubDistrict());
            address.setDistrict(addressRequest.getDistrict());
            address.setSpecificAddress(addressRequest.getSpecificAddress());
            address.setDefault(addressRequest.isLevant());
            address.setCustomer(customer.get());

            if (addressRequest.isLevant()) {
                Address defaultAddress = addressRepository.findByDefaultTrueAndCustomer(customer.get().getId());

                if (defaultAddress != null) {
                    defaultAddress.setDefault(false);

                    addressRepository.save(defaultAddress);
                }
            }

            return addressRepository.save(address);
        }

        return null;
    }

    @Override
    public Address deleteAddress(Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {
            addressRepository.delete(address.get());
            return address.get();
        }
        return null;
    }

    @Override
    public Address editAddress(Long addressId, AddressRequest addressRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Customer> customer = customerRepository.findCustomerByUserName(authentication.getName());

        if (customer.isPresent()) {
            Address address = addressRepository.findById(addressId).get();

            address.setReceiverName(addressRequest.getReceiverName());
            address.setPhone(addressRequest.getPhone());
            address.setEmail(addressRequest.getEmail());
            address.setCity(addressRequest.getCity());
            address.setSubDistrict(addressRequest.getSubDistrict());
            address.setDistrict(addressRequest.getDistrict());
            address.setSpecificAddress(addressRequest.getSpecificAddress());
            address.setDefault(addressRequest.isLevant());
            address.setCustomer(customer.get());

            if (addressRequest.isLevant()) {
                Address defaultAddress = addressRepository.findByDefaultTrueAndCustomer(customer.get().getId());

                if (defaultAddress != null) {
                    defaultAddress.setDefault(false);

                    addressRepository.save(defaultAddress);
                }
            }

            return addressRepository.save(address);
        }

        return null;
    }

    @Override
    public Address getDefaultAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Customer> customer = customerRepository.findCustomerByUserName(authentication.getName());

        return addressRepository.findByDefaultTrueAndCustomer(customer.get().getId());
    }

    @Override
    public List<Address> getListAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Customer> customer = customerRepository.findCustomerByUserName(authentication.getName());

        return customer.get().getAddress().stream().filter(item -> item != getDefaultAddress()).toList();
    }

    @Override
    public CustomCustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Address> addresses = customer.getAddress();

        return new CustomCustomerResponse(customer, addresses);
    }

    @Override
    public Customer editCustomer(Long customerId, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setFullName(customerRequest.getFullName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        customer.setGenderEnum(customerRequest.getGenderEnum());
        customer.setDateOfBirth(customerRequest.getDateOfBirth());

        Address address = addressRepository.findById(customerRequest.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setReceiverName(customerRequest.getAddressRequest().getReceiverName());
        address.setPhone(customerRequest.getAddressRequest().getPhone());
        address.setEmail(customerRequest.getAddressRequest().getEmail());
        address.setCity(customerRequest.getAddressRequest().getCity());
        address.setDistrict(customerRequest.getAddressRequest().getDistrict());
        address.setSubDistrict(customerRequest.getAddressRequest().getSubDistrict());
        address.setSpecificAddress(customerRequest.getAddressRequest().getSpecificAddress());

        addressRepository.save(address);

        return customerRepository.save(customer);
    }


}
