package com.example.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private double price = 1;
    private String currency = "INR";
    private String method;
    private String intent = "sale";
    private String description = "Payment";
}
