package com.harsha.lms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class KeyAccountManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String timeZone;

    @OneToMany(mappedBy = "keyAccountManager", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Lead> leads;

//    public KeyAccountManager(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }

}
