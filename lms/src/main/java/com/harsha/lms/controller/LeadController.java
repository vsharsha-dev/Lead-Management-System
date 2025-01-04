package com.harsha.lms.controller;

import com.harsha.lms.model.Lead;
import com.harsha.lms.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    // Get all leads
    @GetMapping
    public List<Lead> getAllLeads() {
        return leadService.getAllLeads();
    }

    // Get a lead
    @GetMapping("{id}")
    public Lead getLead(@PathVariable Long id) {
        return leadService.getLead(id);
    }

    // Add a lead
    @PostMapping
    public ResponseEntity<?> addLead(@RequestBody Lead lead){
        System.out.println("Received Lead: "+ lead);

        // Add lead via service
        try{
            Lead savedLead = leadService.addLead(lead);
            return new ResponseEntity<>(savedLead, HttpStatus.CREATED);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateLead(@PathVariable int id, @RequestBody Map<String, Object> updates){
        try{
            Lead updatedLead = leadService.updateLead(id, updates);
            return new ResponseEntity<>(updatedLead, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
