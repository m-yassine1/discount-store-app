package com.discount.store.model;

import com.discount.store.retail.util.Constant;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ItemRequest {
    @NotBlank
    @NotNull
    @JsonProperty("name")
    private String name;
    @Positive
    @NotNull
    @JsonProperty("price")
    private double price;
    @NotNull
    @JsonProperty("type")
    private Constant.ItemType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Constant.ItemType getType() {
        return type;
    }

    public void setType(Constant.ItemType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ItemRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", type=" + type +
                '}';
    }
}
