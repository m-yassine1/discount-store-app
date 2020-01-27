package com.discount.store.controller;

import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;
import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;
import com.discount.store.retail.util.Constant;
import com.discount.store.service.CustomerService;
import com.discount.store.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class CustomerControllerTest {
    @InjectMocks
    private ItemService itemService;
    @InjectMocks
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void createCustomer() throws Exception {
        final String name = "John Doe";
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe");
        customerRequest.setType(Constant.CustomerType.employee);
        customerRequest.setCreationDate(creationDate);

        when(customerService.insertCustomer(customerRequest)).thenReturn(customer);
        this.mockMvc.perform(post("/customer-api/v1/customers")
                .content(customer.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void getCustomer() throws Exception {
        final String name = "John Doe";
        LocalDate creationDate = LocalDate.now();
        int customerId = 1;
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);

        when(customerService.getCustomer(customerId)).thenReturn(Optional.of(customer));
        this.mockMvc.perform(get("/customer-api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmptyCustomer() throws Exception {
        int customerId = 1;
        when(customerService.getCustomer(customerId)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/customer-api/v1/customers/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomer() throws Exception {
        final String name = "John Doe";
        int customerId = 1;
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe1");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.now().minusYears(3));

        when(customerService.updateCustomer(customerId, customerRequest)).thenReturn(null);
        this.mockMvc.perform(put("/customer-api/v1/customers/0")
                .content(customer.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateEmptyCustomer() throws Exception {
        final String name = "John Doe";
        int customerId = 1;
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe1");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.now().minusYears(3));

        when(customerService.updateCustomer(customerId, customerRequest)).thenReturn(customer);
        this.mockMvc.perform(put("/customer-api/v1/customers/0")
                .content(customer.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmptyCustomer() throws Exception {
        int customerId = 1;

        when(customerService.deleteCustomer(customerId)).thenReturn(null);
        this.mockMvc.perform(delete("/customer-api/v1/customers/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCustomer() throws Exception {
        final String name = "John Doe";
        int customerId = 1;
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);

        when(customerService.deleteCustomer(customerId)).thenReturn(customer);
        this.mockMvc.perform(delete("/customer-api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createItem() throws Exception {
        int customerId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);

        when(itemService.insertItem(customerId, itemRequest)).thenReturn(item);
        this.mockMvc.perform(post("/customer-api/v1/customers/1/items")
                .content(item.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void getItems() throws Exception {
        int customerId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        List<Item> items = new ArrayList<>();
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        when(itemService.getItems(customerId)).thenReturn(items);
        this.mockMvc.perform(get("/customer-api/v1/customers/1/items")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        when(itemService.getItem(customerId, itemId)).thenReturn(Optional.of(item));
        this.mockMvc.perform(get("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmptyItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        when(itemService.getItem(customerId, itemId)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEmptyItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);

        when(itemService.updateItem(customerId, itemId, itemRequest)).thenReturn(null);
        this.mockMvc.perform(put("/customer-api/v1/customers/1/items/1")
                .content(item.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);

        when(itemService.updateItem(customerId, itemId, itemRequest)).thenReturn(item);
        this.mockMvc.perform(put("/customer-api/v1/customers/1/items/1")
                .content(item.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmptyItem() throws Exception {
        int customerId = 1;
        int itemId = 1;

        when(itemService.deleteItem(customerId, itemId)).thenReturn(null);
        this.mockMvc.perform(delete("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        when(itemService.deleteItem(customerId, itemId)).thenReturn(item);
        this.mockMvc.perform(delete("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}