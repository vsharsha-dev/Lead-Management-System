package com.harsha.lms.service;

import com.harsha.lms.model.KeyAccountManager;
import com.harsha.lms.model.Lead;
import com.harsha.lms.repo.KamRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class KamService {

    @Autowired
    private KamRepo kamRepo;

//    private Map<String, String> statusMap = new HashMap<>();

//    public KamService(){
//        this.statusMap.put("new", "NEW");
//        this.statusMap.put("inprogress", "IN_PROGRESS");
//        this.statusMap.put("won", "WON");
//        this.statusMap.put("lost", "LOST");
//        this.statusMap.put("onhold", "ON_HOLD");
//        this.statusMap.put("archived", "ARCHIVED");
//    }

    public List<KeyAccountManager> getAllKams() {
        return kamRepo.findAll();
    }

    public KeyAccountManager getKamById(Long id) {
        return kamRepo.findById(id.intValue()).orElseThrow(() -> new EntityNotFoundException("KAM with id "+id+" not found"));
    }

    public KeyAccountManager addKam(KeyAccountManager kam) {
        if (kam.getName() == null || kam.getEmail() == null || kam.getTimeZone() == null){
            throw new IllegalArgumentException("KAM fields cannot be null.");
        }
        return kamRepo.save(kam);
    }

    public KeyAccountManager updateKam(Long id, KeyAccountManager kamDetails) {
        return kamRepo.findById(id.intValue()).map(kam -> {
            kam.setName(kamDetails.getName());
            kam.setEmail(kamDetails.getEmail());
            kam.setTimeZone(kamDetails.getTimeZone());
            return kamRepo.save(kam);
        }).orElseThrow(() -> new RuntimeException("KAM not found"));
    }

    public void deleteKam(Long id) {
        kamRepo.deleteById(id.intValue());
    }

    public List<Lead> getLeadsByKamId(Long id) {
        KeyAccountManager kam = kamRepo.findById(id.intValue()).orElseThrow(() -> new EntityNotFoundException("Could not find KAM with id: "+id));
        return kam.getLeads();
    }


    public List<Lead> getLeadsByStatus(String status, Long kamId) {
        KeyAccountManager kam = kamRepo.findById(kamId.intValue()).orElseThrow(() -> new EntityNotFoundException("Could not find KAM with id: "+kamId));
        return kam.getLeads().stream()
                .filter(lead -> String.valueOf(lead.getStatus()).replace("_", "").toLowerCase().equals(status))
                .toList();

    }
}
