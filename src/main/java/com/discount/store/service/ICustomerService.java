package com.discount.store.service;

import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    Customer insertCustomer(CustomerRequest customer);
    Customer updateCustomer(int id, CustomerRequest customer);
    Customer deleteCustomer(int id);
    List<Customer> getCustomers();
    Optional<Customer> getCustomer(int id);
}
