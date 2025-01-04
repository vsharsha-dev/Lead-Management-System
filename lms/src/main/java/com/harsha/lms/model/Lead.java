package com.harsha.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int callFrequency;
    private LocalDateTime lastCallDate;

    private String timeZone; // Stores the lead's time zone

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CallLog> callLogs;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "kam_id")
    private KeyAccountManager keyAccountManager;

    public enum Status {
        NEW, IN_PROGRESS, WON, LOST, ON_HOLD, ARCHIVED;
    }

//    public Lead(String name, String email, Status status, int callFrequency, String timeZone, KeyAccountManager keyAccountManager) {
//        this.name = name;
//        this.email = email;
//        this.status = status;
//        this.callFrequency = callFrequency;
//        this.timeZone = timeZone;
//        this.keyAccountManager = keyAccountManager;
//        this.createdAt = LocalDateTime.now(ZoneId.of(timeZone));
//        this.updatedAt = LocalDateTime.now(ZoneId.of(timeZone));
//    }


}
