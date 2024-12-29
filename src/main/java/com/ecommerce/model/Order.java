package com.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Order {
    @Id
    private Long customerId;
    private LocalDate orderDate;
    private Integer quantity;
    private Double price;
    private String product;
    private Double profit;
    private String paymentMethod;
    private Double shippingCost;
    private Double discount;
    private Double totalPrice;

}
