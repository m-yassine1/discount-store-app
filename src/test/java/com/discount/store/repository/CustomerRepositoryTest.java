package com.discount.store.repository;

import com.discount.store.model.Customer;
import com.discount.store.retail.util.Constant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@WebMvcTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveTest() {
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
