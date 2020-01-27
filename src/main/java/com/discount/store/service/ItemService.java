package com.discount.store.service;

import com.discount.store.exception.ResourceNotFoundException;
import com.discount.store.helper.ItemHelper;
import com.discount.store.model.Customer;
import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;
import com.discount.store.repository.CustomerRepository;
import com.discount.store.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService implements IItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Item insertItem(int customerId, ItemRequest item) {
        if(!ItemHelper.doesCustomerExists(customerRepository, customerId)) {
            throw new ResourceNotFoundException("Customer "+customerId+" does not exist");
        }
        return itemRepository.save(new Item(item.getName(), item.getPrice(), item.getType(), new Customer(customerId)));
    }

    @Override
    public Optional<Item> updateItem(int customerId, int itemId, ItemRequest updateItem) {
        return getItem(customerId, itemId).map(item -> {
            item.setPrice(updateItem.getPrice());
            item.setType(updateItem.getType());
            item.setName(updateItem.getName());
            return Optional.of(itemRepository.save(item));
        }).orElse(Optional.empty());
    }

    @Override
    public Optional<Item> deleteItem(int customerId, int itemId) {
        return getItem(customerId, itemId).map(item -> {
             itemRepository.delete(item);
             return Optional.of(item);
        }).orElse(Optional.empty());
    }

    @Override
    public List<Item> getItems(int customerId) {
        if(!ItemHelper.doesCustomerExists(customerRepository, customerId)) {
            throw new ResourceNotFoundException("Customer "+customerId+" does not exist");
        }
        return itemRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<Item> getItem(int customerId, int itemId) {
        if(!ItemHelper.doesCustomerExists(customerRepository, customerId)) {
            throw new ResourceNotFoundException("Customer "+customerId+" does not exist");
        }
        return itemRepository.findByIdAndCustomerId(itemId, customerId);
    }
}
