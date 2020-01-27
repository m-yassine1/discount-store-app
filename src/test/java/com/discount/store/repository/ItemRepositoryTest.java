package com.discount.store.repository;

import com.discount.store.model.Customer;
import com.discount.store.model.Item;
import com.discount.store.retail.util.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemsRepository;
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

        List<Item> items = new ArrayList<>();
        items.add(new Item("Fridge", 120, Constant.ItemType.electronic, customer));
        items.add(new Item("Apple",50, Constant.ItemType.grocery, customer));
        itemsRepository.saveAll(items);
        List<Item> itemsInDb = itemsRepository.findByCustomerId(customer.getId());
        assertEquals(2, itemsInDb.size());
        assertEquals("Item price must match inserted item price", 120, (int)itemsInDb.get(0).getPrice());
        assertEquals("Item type must match inserted item type", Constant.ItemType.electronic, itemsInDb.get(0).getType());
        assertEquals("Item name must match inserted item name", "Fridge", itemsInDb.get(0).getName());
        assertEquals("Item must belong to the same customer", customer.getId(), itemsInDb.get(0).getCustomer().getId());
    }
}
