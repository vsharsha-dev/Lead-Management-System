package com.harsha.lms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime callDate;
    private String callSummary;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    @JsonBackReference
    private Lead lead;


//    public CallLog(LocalDateTime callDate, String callSummary, Lead lead) {
//        this.callDate = callDate;
//        this.callSummary = callSummary;
//        this.lead = lead;
//    }

}
