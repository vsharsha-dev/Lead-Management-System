package com.harsha.lms.controller;

import com.harsha.lms.model.CallLog;
import com.harsha.lms.model.Contact;
import com.harsha.lms.model.Lead;
import com.harsha.lms.service.CallLogService;
import com.harsha.lms.service.KamService;
import com.harsha.lms.service.LeadService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private KamService kamService;

    @Autowired
    private CallLogService callLogService;

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

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**** Contact Management endpoints within LeadController ****/

    @GetMapping("/contacts/all")
    public ResponseEntity<?> getAllContacts() {
        return new ResponseEntity<>(leadService.getAllContacts(), HttpStatus.OK);
    }

    @GetMapping("/{leadId}/contacts")
    public ResponseEntity<List<Contact>> getContactsForLead(@PathVariable Long leadId) {
        return new ResponseEntity<>(leadService.getContactsForLead(leadId), HttpStatus.OK);
    }

    @PostMapping("/{leadId}/contacts")
    public ResponseEntity<?> addContactToLead(@PathVariable Long leadId, @RequestBody Contact contact) {
        try {
            return new ResponseEntity<>(leadService.addContactToLead(leadId, contact), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{leadId}/contacts/{contactId}")
    public ResponseEntity<?> updateContactForLead(@PathVariable Long leadId, @PathVariable Long contactId, @RequestBody Map<String, Object> contactUpdates) {
        try {
            return new ResponseEntity<>(leadService.updateContactForLead(leadId, contactId, contactUpdates), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{leadId}/contacts/{contactId}")
    public ResponseEntity<?> deleteContactFromLead(@PathVariable Long leadId, @PathVariable Long contactId) {
        try {
            leadService.deleteContactFromLead(leadId, contactId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<?> deleteContact(@PathVariable Long contactId) {
        try {
            leadService.deleteContact(contactId);
            return new ResponseEntity<>("Contact Removed", HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    /**** Key Account Manager endpoints within LeadController ****/

//    // Get all leads of a KAM
//    @GetMapping("kam/{kamId}")
//    public ResponseEntity<?> getLeadsByKamId(@PathVariable Long id) {
//        try{
//            List<Lead> leads = kamService.getLeadsByKamId(id);
//            return new ResponseEntity<>(leads, HttpStatus.OK);
//        }
//        catch (EntityNotFoundException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // Get all leads of a KAM with given status
//    @GetMapping("kam/{kamId}")
//    public ResponseEntity<?> getLeadsByStatus(@RequestParam(name = "status") String status, @PathVariable Long kamId) {
//        try{
//            List<Lead> leads = kamService.getLeadsByStatus(status, kamId);
//            return new ResponseEntity<>(leads, HttpStatus.OK);
//        }
//        catch (EntityNotFoundException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }

    // Get all Leads which require call today
    @GetMapping("/kam/{kamId}/calls-today")
    public ResponseEntity<List<Lead>> getLeadsRequiringCallsToday(@PathVariable Long kamId) {
        List<Lead> leads = leadService.getLeadsRequiringCallsToday(kamId);
        return new ResponseEntity<>(leads, HttpStatus.OK);
    }

    // Add Lead for a particular KAM
    @PostMapping("kam/{kamId}")
    public ResponseEntity<?> addLeadWithKam(@RequestBody Lead lead, @PathVariable Long kamId){
        try{
            Lead addedLead = leadService.addLeadWithKam(lead, kamId);
            return new ResponseEntity<>(addedLead, HttpStatus.OK);
        }
        catch (IllegalArgumentException e1){
            return new ResponseEntity<>(e1.getMessage(), HttpStatus.CONFLICT);
        }
        catch (EntityNotFoundException e2){
            return new ResponseEntity<>(e2.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


//    // Call a Lead
//    @PostMapping("/{leadId}/call")
//    public ResponseEntity<?> callLead(@PathVariable Long leadId){
//
//    }



    /**** Call Logs endpoints within Lead Controller ****/

    // Get all call logs for a lead
    @GetMapping("/{leadId}/call-logs")
    public ResponseEntity<?> getCallLogsByLeadId(@PathVariable Long leadId) {
        try{
            List<CallLog> callLogs = leadService.getCallLogsByLeadId(leadId);
            return new ResponseEntity<>(callLogs, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Make a call to a Lead
    @PostMapping("/{leadId}/call")
    public ResponseEntity<?> addCallLog(@PathVariable Long leadId, @RequestBody CallLog log) {
        try {
            // Add the call to logs
            CallLog callLog = callLogService.addCallLog(leadId, log);

            // Update lastCallDate for the lead
            leadService.callLead(leadId, callLog);

            return new ResponseEntity<>(callLog, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
