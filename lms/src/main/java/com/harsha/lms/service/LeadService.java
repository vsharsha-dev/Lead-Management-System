package com.harsha.lms.service;

import com.harsha.lms.model.Lead;
import com.harsha.lms.repo.LeadRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class LeadService {

    @Autowired
    private LeadRepo leadRepo;


    public List<Lead> getAllLeads() {
        return leadRepo.findAll();
    }

    public Lead addLead(Lead lead) {

        Optional<Lead> existingLead = leadRepo.findByName(lead.getName());
        if(existingLead.isPresent()){
            throw new IllegalArgumentException("Lead with the given name already exists");
        }

        // Add createdAt and updatedAt timestamp
        if(lead.getCreatedAt() == null){
            lead.setCreatedAt(LocalDateTime.now(ZoneId.of(lead.getTimeZone())));
        }
        lead.setUpdatedAt(LocalDateTime.now(ZoneId.of(lead.getTimeZone())));

        return leadRepo.save(lead);
    }

//    public void addLead(Lead lead) {
//        leadRepo.save(lead);
//    }

    public Lead getLead(Long id){
        return leadRepo.findById(id.intValue()).orElseThrow(() -> new EntityNotFoundException("Lead not found with id: "+ id));
    }

    public Lead updateLead(int leadId, Map<String, Object> updates) {
        Lead currentLead =
                leadRepo
                .findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found with id: "+leadId));
        for(String key : updates.keySet()){
            switch (key) {
                case "name" -> currentLead.setName((String) updates.get(key));
                case "email" -> currentLead.setEmail((String) updates.get(key));
                case "status" -> currentLead.setStatus(Lead.Status.valueOf((String) updates.get(key)));
                case "callFrequency" -> currentLead.setCallFrequency((Integer) updates.get(key));
                case "timeZone" -> currentLead.setTimeZone((String) updates.get(key));
                case "lastCallDate" -> currentLead.setLastCallDate(LocalDateTime.parse((String) updates.get(key)));
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        }
        currentLead.setUpdatedAt(LocalDateTime.now(ZoneId.of(currentLead.getTimeZone())));

        return leadRepo.save(currentLead);
    }
}
