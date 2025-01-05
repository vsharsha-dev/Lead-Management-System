package com.harsha.lms.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;
    private String orderDetails;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    @JsonIgnore
    private Lead lead;


//    public Order(LocalDateTime orderDate, String orderDetails, Lead lead) {
//        this.orderDate = orderDate;
//        this.orderDetails = orderDetails;
//        this.lead = lead;
//    }

}