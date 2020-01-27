package com.discount.store.service;

import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;
import com.discount.store.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer insertCustomer(CustomerRequest customer) {
        return customerRepository.save(new Customer(customer.getName(), customer.getCreationDate(), customer.getType()));
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        customers.forEach(Customer::setTotalBill);
        return customers;
    }

    @Override
    public Optional<Customer> getCustomer(int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        customer.ifPresent(Customer::setTotalBill);
        return customer;
    }

    @Override
    public Customer updateCustomer(int id, CustomerRequest updateCustomer) {
        return getCustomer(id).map(customer -> {
            customer.setCreationDate(updateCustomer.getCreationDate());
            customer.setName(updateCustomer.getName());
            customer.setType(updateCustomer.getType());
            return customerRepository.save(customer);
        }).orElse(null);
    }

    @Override
    public Customer deleteCustomer(int id) {
        return getCustomer(id).map(customer -> {
            customerRepository.delete(customer);
            return customer;
        }).orElse(null);
    }
}
