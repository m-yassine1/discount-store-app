package com.discount.store.repository;

import com.discount.store.model.Customer;
import com.discount.store.retail.util.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@DataJpaTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveTest() throws Exception {
        final String name = "John Doe";
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);
        customerRepository.save(customer);
        Optional<Customer> insertedCustomer = customerRepository.findById(customer.getId());
        assertTrue(insertedCustomer.isPresent());
        assertEquals("Customer type must match inserted type", Constant.CustomerType.employee, insertedCustomer.get().getType());
        assertEquals("Names must match", name, insertedCustomer.get().getName());
        assertEquals("Creation Dates must match", creationDate, insertedCustomer.get().getCreationDate());
    }
}
