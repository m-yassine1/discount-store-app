package com.discount.store.service;

import com.discount.store.exception.ResourceNotFoundException;
import com.discount.store.model.Customer;
import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;
import com.discount.store.repository.CustomerRepository;
import com.discount.store.repository.ItemRepository;
import com.discount.store.retail.util.Constant;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceTests {
    @Mock
    private ItemRepository itemsRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void fetchItems() throws Exception {
        int customerId = 1;
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        when(itemsRepository.findByCustomerId(customerId)).thenReturn(items);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        List<Item> fetchedItems = itemService.getItems(customerId);
        assertEquals("Item list is not empty", items.size(), fetchedItems.size());
    }

    @Test
    public void fetchNoneExistentUserItems() {
        int customerId = 1;
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        when(itemsRepository.findByCustomerId(customerId)).thenReturn(items);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            itemService.getItems(customerId);
        });
        assertTrue(exception.getMessage().contains("Customer"));
    }

    @Test
    public void fetchEmptyItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.empty());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        Optional<Item> item = itemService.getItem(customerId, itemId);
        assertFalse(item.isPresent());
    }

    @Test
    public void fetchNoneExistentUserItem() {
        int customerId = 1;
        int itemId = 1;
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.empty());
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            itemService.getItem(customerId, itemId);
        });
        assertTrue(exception.getMessage().contains("Customer"));
    }

    @Test
    public void fetchItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Fridge", 120, Constant.ItemType.electronic, customer);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.of(item));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Optional<Item> fetchedItem = itemService.getItem(customerId, itemId);
        assertTrue(fetchedItem.isPresent());
        assertEquals("Name do not match", item.getName(), fetchedItem.get().getName());
        assertEquals("Price do not match", (int)item.getPrice(), (int)fetchedItem.get().getPrice());
        assertEquals("Item of the same type", item.getType(), fetchedItem.get().getType());
    }

    @Test
    public void updateItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Fridge", 120, Constant.ItemType.electronic, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.of(item));
        when(itemsRepository.save(item)).thenReturn(new Item("Microwave", 30, Constant.ItemType.fabric));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Optional<Item> fetchedItem = itemService.updateItem(customerId, itemId, itemRequest);
        assertTrue(fetchedItem.isPresent());
        assertEquals("Name is not the same", item.getName(), fetchedItem.get().getName());
        assertEquals("Price is not the same", (int)item.getPrice(), (int)fetchedItem.get().getPrice());
        assertEquals("Type is not the same", item.getType(), fetchedItem.get().getType());
    }

    @Test
    public void updateEmptyItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.empty());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        Optional<Item> item = itemService.updateItem(customerId, itemId, itemRequest);
        assertFalse(item.isPresent());
    }

    @Test
    public void deleteItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Fridge", 120, Constant.ItemType.electronic, customer);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.of(item));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Optional<Item> fetchedItem = itemService.deleteItem(customerId, itemId);
        assertTrue(fetchedItem.isPresent());
        assertEquals("Name is not the same", item.getName(), fetchedItem.get().getName());
        assertEquals("Price is not the same", (int)item.getPrice(), (int)fetchedItem.get().getPrice());
        assertEquals("Type is not the same", item.getType(), fetchedItem.get().getType());
    }

    @Test
    public void deleteEmptyItem() throws Exception {
        int customerId = 1;
        int itemId = 1;
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.empty());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        Optional<Item> item = itemService.deleteItem(customerId, itemId);
        assertFalse(item.isPresent());
    }
}
