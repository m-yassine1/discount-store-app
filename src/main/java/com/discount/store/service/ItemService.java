package com.discount.store.service;

import com.discount.store.model.Customer;
import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;
import com.discount.store.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService implements IItemService {

    @Autowired
    ItemRepository itemRepository;

    @Override
    public Item insertItem(int customerId, ItemRequest item) {
        return itemRepository.save(new Item(item.getName(), item.getPrice(), item.getType(), new Customer(customerId)));
    }

    @Override
    public Item updateItem(int customerId, int itemId, ItemRequest updateItem) {
        return getItem(customerId, itemId).map(item -> {
            item.setPrice(updateItem.getPrice());
            item.setType(updateItem.getType());
            item.setName(updateItem.getName());
            return itemRepository.save(item);
        }).orElse(null);
    }

    @Override
    public Item deleteItem(int customerId, int itemId) {
        return getItem(customerId, itemId).map(item -> {
             itemRepository.delete(item);
             return item;
        }).orElse(null);
    }

    @Override
    public List<Item> getItems(int customerId) {
        return itemRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<Item> getItem(int customerId, int itemId) {
        return itemRepository.findByIdAndCustomerId(itemId, customerId);
    }
}
