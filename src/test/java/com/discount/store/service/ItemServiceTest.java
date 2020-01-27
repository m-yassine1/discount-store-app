package com.discount.store.service;

import com.discount.store.model.Customer;
import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;
import com.discount.store.repository.ItemRepository;
import com.discount.store.retail.util.Constant;
import com.discount.store.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceTest {
    @Mock
    private ItemRepository itemsRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void fetchItems() {
        int customerId = 1;
        List<Item> items = new ArrayList<>();
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        when(itemsRepository.findByCustomerId(customerId)).thenReturn(items);
        List<Item> fetchedItems = itemService.getItems(customerId);
        assertEquals("Item list is not empty", items.size(), fetchedItems.size());
    }

    @Test
    public void fetchEmptyItem() {
        int customerId = 1;
        int itemId = 1;
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.empty());
        Optional<Item> item = itemService.getItem(customerId, itemId);
        assertFalse(item.isPresent());
    }

    @Test
    public void fetchItem() {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Fridge", 120, Constant.ItemType.electronic, customer);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.of(item));
        Optional<Item> fetchedItem = itemService.getItem(customerId, itemId);
        assertTrue(fetchedItem.isPresent());
        assertEquals("Name do not match", item.getName(), fetchedItem.get().getName());
        assertEquals("Price do not match", (int)item.getPrice(), (int)fetchedItem.get().getPrice());
        assertEquals("Item of the same type", item.getType(), fetchedItem.get().getType());
    }

    @Test
    public void updateItem() {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Fridge", 120, Constant.ItemType.electronic, customer);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.of(item));
        Item fetchedItem = itemService.updateItem(customerId, itemId, itemRequest);
        assertNotNull(item);
        assertEquals("Name is not the same", item.getName(), fetchedItem.getName());
        assertEquals("Price is not the same", (int)item.getPrice(), (int)fetchedItem.getPrice());
        assertEquals("Type is not the same", item.getType(), fetchedItem.getType());
    }

    @Test
    public void updateEmptyItem() {
        int customerId = 1;
        int itemId = 1;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Microwave");
        itemRequest.setPrice(30);
        itemRequest.setType(Constant.ItemType.fabric);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.empty());
        Item item = itemService.updateItem(customerId, itemId, itemRequest);
        assertNull(item);
    }

    @Test
    public void deleteItem() {
        int customerId = 1;
        int itemId = 1;
        Customer customer = new Customer("John Doe", LocalDate.now(), Constant.CustomerType.employee);
        Item item = new Item("Fridge", 120, Constant.ItemType.electronic, customer);
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.of(item));
        Item fetchedItem = itemService.deleteItem(customerId, itemId);
        assertNotNull(item);
        assertEquals("Name is not the same", item.getName(), fetchedItem.getName());
        assertEquals("Price is not the same", (int)item.getPrice(), (int)fetchedItem.getPrice());
        assertEquals("Type is not the same", item.getType(), fetchedItem.getType());
    }

    @Test
    public void deleteEmptyItem() {
        int customerId = 1;
        int itemId = 1;
        when(itemsRepository.findByIdAndCustomerId(itemId, customerId)).thenReturn(Optional.empty());
        Item item = itemService.deleteItem(customerId, itemId);
        assertNull(item);
    }
}
