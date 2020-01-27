package com.discount.store.service;

import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;
import com.discount.store.model.Item;
import com.discount.store.repository.CustomerRepository;
import com.discount.store.retail.util.Constant;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void fetchEmptyCustomer() throws Exception {
        int customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Optional<Customer> fetchedCustomer = customerService.getCustomer(customerId);
        assertFalse(fetchedCustomer.isPresent());
    }

    @Test
    public void fetchCustomer() throws Exception {
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
    public void fetchCustomers() throws Exception {
        Customer customer1 = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Customer customer2 = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        when(customerRepository.findAll()).thenReturn(customers);
        assertEquals("Customers are not of the same length", 2, customers.size());
     }

    @Test
    public void updateCustomer() throws Exception {
        int customerId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Test");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.parse("2017-02-06"));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(new Customer("Test", LocalDate.parse("2017-02-06"), Constant.CustomerType.affiliate));
        Optional<Customer> fetchedCustomer = customerService.updateCustomer(customerId, customerRequest);
        assertTrue(fetchedCustomer.isPresent());
        assertEquals("Customer type must match inserted type", Constant.CustomerType.affiliate, fetchedCustomer.get().getType());
        assertEquals("Names must match", customerRequest.getName(), fetchedCustomer.get().getName());
        assertEquals("Creation Dates must match", customerRequest.getCreationDate(), fetchedCustomer.get().getCreationDate());
    }

    @Test
    public void updateEmptyCustomer() throws Exception {
        int customerId = 1;
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Test");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.parse("2017-02-06"));
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Optional<Customer> fetchedCustomer = customerService.updateCustomer(customerId, customerRequest);
        assertFalse(fetchedCustomer.isPresent());
    }

    @Test
    public void deleteCustomer() throws Exception {
        int customerId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);
        Optional<Customer> fetchedCustomer = customerService.deleteCustomer(customerId);
        assertTrue(fetchedCustomer.isPresent());
        assertEquals("Customer type must match inserted type", Constant.CustomerType.employee, fetchedCustomer.get().getType());
        assertEquals("Names must match", customer.getName(), fetchedCustomer.get().getName());
        assertEquals("Creation Dates must match", customer.getCreationDate(), fetchedCustomer.get().getCreationDate());
    }

    @Test
    public void deleteEmptyCustomer() throws Exception {
        int customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        doNothing().when(customerRepository).delete(new Customer());
        Optional<Customer> fetchedCustomer = customerService.deleteCustomer(customerId);
        assertFalse(fetchedCustomer.isPresent());
     }

     @Test
    public void getEmployeePayout() throws Exception {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(165), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
     }

     @Test
     public void getAffiliatePayout() throws Exception {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.affiliate);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(80).stripTrailingZeros(), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getOldCustomerPayout() throws Exception {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now().minusYears(2), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(80).stripTrailingZeros(), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getNewCustomerPayout() throws Exception {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(80).stripTrailingZeros(), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getNewCustomerWithIntervalDiscountPayout() throws Exception {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 30, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",210, Constant.ItemType.electronic, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(230).stripTrailingZeros(), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }

    @Test
    public void getNewCustomerWithGroceryPayout() throws Exception {
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.normal);
        items.add(new Item("Fridge", 100, Constant.ItemType.grocery, customer));
        items.add(new Item("Apple",10, Constant.ItemType.grocery, customer));
        customer.setItems(items);
        assertEquals("Invalid bill", BigDecimal.valueOf(105), BigDecimal.valueOf(customer.getTotalBill()).stripTrailingZeros());
    }
}
