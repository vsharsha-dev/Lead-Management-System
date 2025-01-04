package com.harsha.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contact {

    public enum Role {
        OWNER, MANAGER, PROCUREMENT, FINANCE, CHEF;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

//    public Contact(String name, Role role, String email, String phone, Lead lead) {
//        this.name = name;
//        this.role = role;
//        this.email = email;
//        this.phone = phone;
//        this.lead = lead;
//    }
}