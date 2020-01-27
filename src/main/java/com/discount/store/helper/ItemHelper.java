package com.discount.store.helper;

import com.discount.store.repository.CustomerRepository;

public class ItemHelper {
    public static boolean doesCustomerExists(CustomerRepository customerRepository, int customerId) {
        return customerRepository.findById(customerId).isPresent();
    }
}
