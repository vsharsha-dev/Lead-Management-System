package com.harsha.lms.controller;

import com.harsha.lms.model.KeyAccountManager;
import com.harsha.lms.model.Lead;
import com.harsha.lms.service.KamService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/kams")
public class KamController {

    @Autowired
    private KamService kamService;

    @GetMapping
    public List<KeyAccountManager> getAllKams(){
        return kamService.getAllKams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKamById(@PathVariable Long id) {
        try{
            KeyAccountManager kam = kamService.getKamById(id);
            return new ResponseEntity<>(kam, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<?> addKam(@RequestBody KeyAccountManager kam) {
        try{
            KeyAccountManager addedKam = kamService.addKam(kam);
            return new ResponseEntity<>(addedKam, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<KeyAccountManager> updateKam(@PathVariable Long id, @RequestBody KeyAccountManager kamDetails) {
        try {
            return new ResponseEntity<>(kamService.updateKam(id, kamDetails), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKam(@PathVariable Long id) {
        kamService.deleteKam(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all Leads of a KAM
    @GetMapping("/{id}/leads")
    public ResponseEntity<?> getLeadsByKamId(@PathVariable Long id) {
        try{
            List<Lead> leads = kamService.getLeadsByKamId(id);
            return new ResponseEntity<>(leads, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
