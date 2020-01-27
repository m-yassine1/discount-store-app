package com.discount.store.model;

import com.discount.store.retail.util.Constant;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public class CustomerRequest {
    @NotBlank
    @NotNull
    @JsonProperty("name")
    private String name;
    @PastOrPresent
    @JsonProperty("creationDate")
    private LocalDate creationDate;
    @NotNull
    @JsonProperty("type")
    private Constant.CustomerType type;

    public Constant.CustomerType getType() {
        return type;
    }

    public void setType(Constant.CustomerType type) {
        this.type = type;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate == null ? LocalDate.now() : creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CustomerRequest{" +
                "name='" + name + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
