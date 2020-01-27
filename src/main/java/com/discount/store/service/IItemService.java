package com.discount.store.service;

import com.discount.store.model.Item;
import com.discount.store.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface IItemService {
    Item insertItem(int customerId, ItemRequest item);
    Optional<Item> updateItem(int customerId, int itemId, ItemRequest item);
    Optional<Item> deleteItem(int customerId, int itemId);
    List<Item> getItems(int customerId);
    Optional<Item> getItem(int customerId, int itemId);
}
