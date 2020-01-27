package com.discount.store.repository;

import com.discount.store.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByCustomerId(int customerId);
    Optional<Item> findByIdAndCustomerId(int id, int customerId);
}
