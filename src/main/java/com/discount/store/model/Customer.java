package com.discount.store.model;

import com.discount.store.helper.CustomerHelper;
import com.discount.store.retail.util.Constant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    protected int id;
    @Column(name = "name")
    protected String name;
    @OneToMany(targetEntity=Item.class, mappedBy="customer", fetch=FetchType.EAGER)
    @JsonIgnoreProperties("customer")
    protected List<Item> items;
    @Transient
    protected double totalBill;
    @Column(name = "creation_date")
    protected LocalDate creationDate;
    @Column(name = "percentage_discount")
    protected int percentageDiscount;
    @Column(name = "type")
    protected Constant.CustomerType type;

    public Customer() {

    }

    public Customer(int id) {
        setId(id);
    }

    public Customer(String name, LocalDate creationDate, Constant.CustomerType type) {
        setName(name);
        setCreationDate(creationDate);
        setType(type);
        setPercentageDiscount();
        setItems(null);
    }

    public Customer(String name, LocalDate creationDate, Constant.CustomerType type, List<Item> items) {
        setName(name);
        setCreationDate(creationDate);
        setType(type);
        setPercentageDiscount();
        setItems(items);
    }

    public Constant.CustomerType getType() {
        return type;
    }

    public void setType(Constant.CustomerType type) {
        this.type = type;
        setPercentageDiscount();
    }

    public int getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount() {
        this.percentageDiscount = getType() == null ?
                CustomerHelper.getPercentageDiscountByDate(this)
                : CustomerHelper.getPercentageDiscount(this);
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        setPercentageDiscount();
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items == null ? new ArrayList<>() : items;
        setTotalBill();
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill() {
        this.totalBill = CustomerHelper.getTotalBill(this);
    }

    @Override
    public String toString() {
        return "{" +
                " \"id\":" + id +
                ", \"name\": \"" + name + "\"" +
                ", \"items\": " + items  +
                ", \"totalBill\": " + totalBill +
                ", \"creationDate\": \"" + creationDate + "\"" +
                ", \"type\": \"" + type + "\"" +
                ", \"percentageDiscount\": " + percentageDiscount +
                "}";
    }
}
