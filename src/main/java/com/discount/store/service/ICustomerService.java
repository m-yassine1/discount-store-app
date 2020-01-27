package com.discount.store.service;

import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    Customer insertCustomer(CustomerRequest customer);
    Optional<Customer> updateCustomer(int id, CustomerRequest customer);
    Optional<Customer> deleteCustomer(int id);
    List<Customer> getCustomers();
    Optional<Customer> getCustomer(int id);
}
