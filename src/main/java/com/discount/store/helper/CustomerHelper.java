package com.discount.store.helper;

import com.discount.store.model.Customer;
import com.discount.store.model.Item;
import com.discount.store.retail.util.Constant;

import java.time.LocalDate;

public class CustomerHelper {
    public static int getPercentageDiscount(Customer customer) {
        switch (customer.getType()) {
            case normal:
                return customer.getCreationDate().isBefore(LocalDate.now().minusYears(2)) ? 5 : 0;
            case employee:
                return 30;
            case affiliate:
                return 10;
            default:
                throw new IllegalStateException("Unexpected value: " + customer.getType());
        }
    }

    public static double getTotalBill(Customer customer) {
        double totalBill = customer.getItems().stream()
                .filter(item -> item.getType() == Constant.ItemType.grocery)
                .mapToDouble(Item::getPrice)
                .sum();

        totalBill += customer.getItems().stream()
                .filter(item -> item.getType() != Constant.ItemType.grocery)
                .mapToDouble(Item::getPrice)
                .sum();

        return totalBill - (int)totalBill / 100 * 5;
    }
}
