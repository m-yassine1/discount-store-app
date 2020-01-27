package com.discount.store.service;

import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;
import com.discount.store.model.Item;
import com.discount.store.repository.CustomerRepository;
import com.discount.store.retail.util.Constant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void fetchEmptyCustomer() {
        int customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Optional<Customer> fetchedCustomer = customerService.getCustomer(customerId);
        assertFalse(fetchedCustomer.isPresent());
    }

    @Test
    public void fetchCustomer() {
        int customerId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Optional<Customer> fetchedCustomer = customerService.getCustomer(customerId);
        assertTrue(fetchedCustomer.isPresent());
        assertEquals("Customer type must match inserted type", Constant.CustomerType.employee, fetchedCustomer.get().getType());
        assertEquals("Names must match", customer.getName(), fetchedCustomer.get().getName());
        assertEquals("Creation Dates must match", customer.getCreationDate(), fetchedCustomer.get().getCreationDate());
    }

    @Test
    public void fetchCustomers() {
        Customer customer1 = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Customer customer2 = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        when(customerRepository.findAll()).thenReturn(customers);
        assertEquals("Customers are not of the same length", 2, customers.size());
     }

    @Test
    public void updateCustomer() {
        int customerId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Test");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.parse("2017-02-06"));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer fetchedCustomer = customerService.updateCustomer(customerId, customerRequest);
        assertNotNull(fetchedCustomer);
        assertEquals("Customer type must match inserted type", Constant.CustomerType.affiliate, fetchedCustomer.getType());
        assertEquals("Names must match", customerRequest.getName(), fetchedCustomer.getName());
        assertEquals("Creation Dates must match", customerRequest.getCreationDate(), fetchedCustomer.getCreationDate());
    }

    @Test
    public void updateEmptyCustomer() {
        int customerId = 1;
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Test");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.parse("2017-02-06"));
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Customer fetchedCustomer = customerService.updateCustomer(customerId, customerRequest);
        assertNull(fetchedCustomer);
    }

    @Test
    public void deleteCustomer() {
        int customerId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer fetchedCustomer = customerService.deleteCustomer(customerId);
        assertNotNull(fetchedCustomer);
        assertEquals("Customer type must match inserted type", Constant.CustomerType.affiliate, fetchedCustomer.getType());
        assertEquals("Names must match", customerRequest.getName(), fetchedCustomer.getName());
        assertEquals("Creation Dates must match", customerRequest.getCreationDate(), fetchedCustomer.getCreationDate());
    }

    @Test
    public void deleteEmptyCustomer() {
        int customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Customer fetchedCustomer = customerService.deleteCustomer(customerId);
        assertNull(fetchedCustomer);
     }

     @Test
    public void getEmployeePayout() {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(200), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
     }

     @Test
     public void getAffiliatePayout() {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.affiliate);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(200), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getOldCustomerPayout() {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now().minusYears(2), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(200), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getNewCustomerPayout() {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(80), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getNewCustomerWithIntervalDiscountPayout() {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",210, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(230), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getNewCustomerWithGroceryPayout() {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 100, Constant.ItemType.grocery, customer));
        items.add(new Item("Apple",10, Constant.ItemType.grocery, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(105), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }
}
