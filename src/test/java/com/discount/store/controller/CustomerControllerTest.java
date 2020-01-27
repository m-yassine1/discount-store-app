package com.discount.store.controller;

import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;
import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;
import com.discount.store.retail.util.Constant;
import com.discount.store.service.CustomerService;
import com.discount.store.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @MockBean
    private ItemService itemService;
    @MockBean
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

        when(customerService.insertCustomer(any(CustomerRequest.class))).thenReturn(customer);
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
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);

        when(customerService.getCustomer(anyInt())).thenReturn(Optional.of(customer));
        this.mockMvc.perform(get("/customer-api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmptyCustomer() throws Exception {
        when(customerService.getCustomer(anyInt())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/customer-api/v1/customers/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEmptyCustomer() throws Exception {
        final String name = "John Doe";
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe1");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.now().minusYears(3));

        when(customerService.updateCustomer(anyInt(), any(CustomerRequest.class))).thenReturn(Optional.empty());
        this.mockMvc.perform(put("/customer-api/v1/customers/0")
                .content(customer.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomer() throws Exception {
        final String name = "John Doe";
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe1");
        customerRequest.setType(Constant.CustomerType.affiliate);
        customerRequest.setCreationDate(LocalDate.now().minusYears(3));

        when(customerService.updateCustomer(anyInt(), any(CustomerRequest.class))).thenReturn(Optional.of(customer));
        this.mockMvc.perform(put("/customer-api/v1/customers/1")
                .content(customer.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmptyCustomer() throws Exception {
        when(customerService.deleteCustomer(anyInt())).thenReturn(Optional.empty());
        this.mockMvc.perform(delete("/customer-api/v1/customers/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCustomer() throws Exception {
        final String name = "John Doe";
        LocalDate creationDate = LocalDate.now();
        Customer customer = new Customer(name, creationDate, Constant.CustomerType.employee);

        when(customerService.deleteCustomer(anyInt())).thenReturn(Optional.of(customer));
        this.mockMvc.perform(delete("/customer-api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createItem() throws Exception {
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);

        when(itemService.insertItem(anyInt(), any(ItemRequest.class))).thenReturn(item);
        this.mockMvc.perform(post("/customer-api/v1/customers/1/items")
                .content(item.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void getItems() throws Exception {
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        List<Item> items = new ArrayList<>();
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        when(itemService.getItems(anyInt())).thenReturn(items);
        this.mockMvc.perform(get("/customer-api/v1/customers/1/items")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getItem() throws Exception {
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        when(itemService.getItem(anyInt(), anyInt())).thenReturn(Optional.of(item));
        this.mockMvc.perform(get("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmptyItem() throws Exception {
        when(itemService.getItem(anyInt(), anyInt())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEmptyItem() throws Exception {
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);

        when(itemService.updateItem(anyInt(), anyInt(), any(ItemRequest.class))).thenReturn(Optional.empty());
        this.mockMvc.perform(put("/customer-api/v1/customers/1/items/1")
                .content(item.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateItem() throws Exception {
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);

        when(itemService.updateItem(anyInt(), anyInt(), any(ItemRequest.class))).thenReturn(Optional.of(item));
        this.mockMvc.perform(put("/customer-api/v1/customers/1/items/1")
                .content(item.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmptyItem() throws Exception {
        when(itemService.deleteItem(anyInt(), anyInt())).thenReturn(Optional.empty());
        this.mockMvc.perform(delete("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteItem() throws Exception {
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Microwave", 30, Constant.ItemType.fabric, customer);
        when(itemService.deleteItem(anyInt(), anyInt())).thenReturn(Optional.of(item));
        this.mockMvc.perform(delete("/customer-api/v1/customers/1/items/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}