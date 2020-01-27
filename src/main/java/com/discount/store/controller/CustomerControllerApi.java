package com.discount.store.controller;

import com.discount.store.exception.ResourceNotFoundException;
import com.discount.store.model.Customer;
import com.discount.store.model.CustomerRequest;
import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;
import com.discount.store.service.CustomerService;
import com.discount.store.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("customer-api/v1/customers")
@RestController
public class CustomerControllerApi {
    private final CustomerService customerService;
    private final ItemService itemService;

    @Autowired
    public CustomerControllerApi(CustomerService customerService, ItemService itemService) {
        this.customerService = customerService;
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Customer insertCustomer(@Valid @RequestBody CustomerRequest customer) {
        return customerService.insertCustomer(customer);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping(path = "{customerId}")
    public Customer getCustomer(@PathVariable("customerId") int customerId) {
        Optional<Customer> customer = customerService.getCustomer(customerId);
        if(!customer.isPresent()) {
            throw new ResourceNotFoundException("Customer " + customerId + " not found");
        }
        return customer.get();
    }

    @PutMapping(path = "{customerId}")
    public Customer updateCustomer(@PathVariable("customerId") int customerId, @Valid @RequestBody CustomerRequest updateCustomer) {
        Optional<Customer> customer = customerService.updateCustomer(customerId, updateCustomer);
        if(!customer.isPresent()) {
            throw new ResourceNotFoundException("Customer " + customerId + " not found");
        }
        return customer.get();
    }

    @DeleteMapping(path = "{customerId}")
    public Customer deleteCustomer(@PathVariable("customerId") int customerId) {
        Optional<Customer> customer = customerService.deleteCustomer(customerId);
        if(!customer.isPresent()) {
            throw new ResourceNotFoundException("Customer " + customerId + " not found");
        }
        return customer.get();
    }

    @PostMapping(path = "{customerId}/items")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Item createItem(@PathVariable("customerId") int customerId, @Valid @RequestBody ItemRequest item) {
        return itemService.insertItem(customerId, item);
    }

    @GetMapping(path = "{customerId}/items")
    public List<Item> getItems(@PathVariable("customerId") int customerId) {
        return itemService.getItems(customerId);
    }

    @GetMapping(path = "{customerId}/items/{itemId}")
    public Item getItem(@PathVariable("customerId") int customerId, @PathVariable("itemId") int itemId) {
        Optional<Item> item = itemService.getItem(customerId, itemId);
        if(!item.isPresent()) {
            throw new ResourceNotFoundException("Customer " + customerId + " does not have item " + itemId);
        }
        return item.get();
     }

    @PutMapping(path = "{customerId}/items/{itemId}")
    public Item updateItem(@PathVariable("customerId") int customerId, @PathVariable("itemId") int itemId, @Valid @RequestBody ItemRequest updateItem) {
        Optional<Item> item = itemService.updateItem(customerId, itemId, updateItem);
        if(!item.isPresent()) {
            throw new ResourceNotFoundException("Customer " + customerId + " does not have item " + itemId);
        }
        return item.get();
    }

    @DeleteMapping(path = "{customerId}/items/{itemId}")
    public Item deleteItem(@PathVariable("customerId") int customerId, @PathVariable("itemId") int itemId) {
        Optional<Item> item = itemService.deleteItem(customerId, itemId);
        if(!item.isPresent()) {
            throw new ResourceNotFoundException("Customer " + customerId + " does not have item " + itemId);
        }
        return item.get();
    }
}
