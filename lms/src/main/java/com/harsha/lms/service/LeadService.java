package com.harsha.lms.service;

import com.harsha.lms.model.CallLog;
import com.harsha.lms.model.Contact;
import com.harsha.lms.model.KeyAccountManager;
import com.harsha.lms.model.Lead;
import com.harsha.lms.repo.ContactRepo;
import com.harsha.lms.repo.KamRepo;
import com.harsha.lms.repo.LeadRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeadService {

    @Autowired
    private LeadRepo leadRepo;

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private KamRepo kamRepo;


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

    public Lead getLead(Long id){
        return leadRepo.findById(id.intValue()).orElseThrow(() -> new EntityNotFoundException("Lead not found with id: "+ id));
    }

    public void deleteLead(Long id) {
        leadRepo.deleteById(id.intValue());
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

    // Get Today's Required Call Count
    public List<Lead> getLeadsRequiringCallsToday(Long kamId) {
//        KeyAccountManager kam = kamRepo.findById(kamId.intValue()).orElseThrow(() -> new EntityNotFoundException("KAM with id "+kamId+" not found"));
//        ZoneId kamZoneId = ZoneId.of(kam.getTimeZone());
//        LocalDateTime currentKamZoneTime = LocalDateTime.now(kamZoneId);
//        return leadRepo.findAll().stream()
//                .filter(lead -> {
//                    if(lead.getKeyAccountManager() != null){
//                        ZoneId leadZoneId = ZoneId.of(lead.getTimeZone());
//
//                        // Convert lastCallDate to LeadTimeZone and add callFrequency
//                        ZonedDateTime kamZoneLeadLastCallDate = ZonedDateTime.of(lead.getLastCallDate(), kamZoneId);
//                        ZonedDateTime leadZoneLeadLastCallDate = kamZoneLeadLastCallDate.withZoneSameInstant(leadZoneId);
//                        ZonedDateTime thresholdDate = leadZoneLeadLastCallDate.plusDays(lead.getCallFrequency());
//
//                        // Convert Threshold date again to KAM time zone and compare with current KAM date
//                        ZonedDateTime thresholdDateInKamZone = thresholdDate.withZoneSameInstant(kamZoneId);
//                        return thresholdDateInKamZone.toLocalDate().equals(currentKamZoneTime.toLocalDate());
//                    }
//                    return false;
//                })
//                .toList();

        KeyAccountManager kam = kamRepo.findById(kamId.intValue()).orElseThrow(() -> new EntityNotFoundException("KAM with id "+kamId+" not found"));
        ZoneId kamZoneId = ZoneId.of(kam.getTimeZone());
        return leadRepo.findAll().stream()
                .filter(lead -> {
                    if(lead.getStatus().equals(Lead.Status.IN_PROGRESS) && lead.getKeyAccountManager().getId().equals(kamId)){
                        LocalDateTime lastCallDateInKamTime = lead.getLastCallDate();
                        LocalDateTime thresholdDate = lastCallDateInKamTime.plusDays(lead.getCallFrequency());
                        return thresholdDate.toLocalDate().equals(LocalDateTime.now(kamZoneId).toLocalDate());
                    }
                    return false;
                })
                .toList();

    }



    /**** Contact related services ****/
    public Contact addContactToLead(Long leadId, Contact contact) {
        return leadRepo.findById(leadId.intValue()).map(lead -> {
            contact.setLead(lead);
            lead.getContacts().add(contact);
            leadRepo.save(lead);
            return contact;
        }).orElseThrow(() -> new RuntimeException("Lead not found"));
    }

    public List<Contact> getContactsForLead(Long leadId) {
        return leadRepo.findById(leadId.intValue()).map(Lead::getContacts)
                .orElseThrow(() -> new RuntimeException("Lead not found"));
    }

    public String updateContactForLead(Long leadId, Long contactId, Map<String, Object> contactUpdates){
        Lead lead = leadRepo.findById(leadId.intValue()).orElseThrow(() -> new RuntimeException("Lead not found"));

        for(Contact contact : lead.getContacts()){
            if(contact.getId().equals(contactId)){
                for(String key : contactUpdates.keySet()){
                    switch (key) {
                        case "name" -> contact.setName((String) contactUpdates.get(key));
                        case "email" -> contact.setEmail((String) contactUpdates.get(key));
                        case "phone" -> contact.setPhone((String) contactUpdates.get(key));
                        case "role" -> contact.setRole((Contact.Role) contactUpdates.get(key));
                        case "lead" -> contact.setLead((Lead) contactUpdates.get(key));
                        default -> throw new IllegalArgumentException("Invalid field: " + key);
                    }
                }
                leadRepo.save(lead);
                return "Modified the contact with id:"+contactId;
            }
        }

        return "Contact with id "+contactId+" not found";
    }

    public void deleteContactFromLead(Long leadId, Long contactId) {
        Lead lead = leadRepo.findById(leadId.intValue()).orElseThrow(() -> new RuntimeException("Lead not found"));
        lead.getContacts().removeIf(contact -> contact.getId().equals(contactId));
        leadRepo.save(lead);
    }

    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    // Method to delete a contact directly through contact repo
    public void deleteContact(Long contactId) {
        contactRepo.deleteById(contactId.intValue());
    }




    /**** Key Account Manager related services ****/

    public Lead addLeadWithKam(Lead lead, Long kamId) {
        Optional<Lead> existingLead = leadRepo.findByName(lead.getName());
        if(existingLead.isPresent()){
            throw new IllegalArgumentException("Lead with the given name already exists");
        }

        // Assign KAM
        KeyAccountManager kam = kamRepo.findById(kamId.intValue()).orElseThrow(() -> new EntityNotFoundException("Key Account Manager with id "+kamId+" not found"));
        lead.setKeyAccountManager(kam);

        // Add createdAt and updatedAt timestamp based on KAM time zone
        if(lead.getCreatedAt() == null){
            lead.setCreatedAt(LocalDateTime.now(ZoneId.of(lead.getKeyAccountManager().getTimeZone())).truncatedTo(ChronoUnit.SECONDS));
        }
        lead.setUpdatedAt(LocalDateTime.now(ZoneId.of(lead.getKeyAccountManager().getTimeZone())).truncatedTo(ChronoUnit.SECONDS));


        return leadRepo.save(lead);

    }

    public List<CallLog> getCallLogsByLeadId(Long leadId) {
        Lead lead = leadRepo.findById(leadId.intValue()).orElseThrow(() -> new EntityNotFoundException("Lead with id "+ leadId+ " not found"));
        return lead.getCallLogs();
    }

    // Method to make a call to Lead (just updating the lastCallDate)
    public void callLead(Long leadId, CallLog log) {
        Lead l = leadRepo.findById(leadId.intValue()).orElseThrow(() -> new EntityNotFoundException("Lead with id "+leadId+" not found"));

        l.setLastCallDate(log.getCallDate());

        leadRepo.save(l);
    }
}
