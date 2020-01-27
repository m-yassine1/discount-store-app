package com.discount.store.model;

import com.discount.store.retail.util.Constant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "type")
    private Constant.ItemType type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("items")
    private Customer customer;

    public Item(String name, double price, Constant.ItemType type, Customer customer) {
        setName(name);
        setPrice(price);
        setType(type);
        setCustomer(customer);
    }

    public Item(String name, double price, Constant.ItemType type) {
        setName(name);
        setPrice(price);
        setType(type);
    }

    public Item() {

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        return "{" +
                " \"id\": " + id +
                ", \"name\": \"" + name + "\"" +
                ", \"price\": " + price +
                ", \"type\": \"" + type + "\"" +
                "}";
    }
}
