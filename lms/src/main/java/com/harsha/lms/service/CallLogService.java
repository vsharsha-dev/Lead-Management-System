package com.harsha.lms.service;

import com.harsha.lms.model.CallLog;
import com.harsha.lms.model.Lead;
import com.harsha.lms.repo.CallLogRepo;
import com.harsha.lms.repo.LeadRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
public class CallLogService {

    @Autowired
    private CallLogRepo callLogRepo;

    @Autowired
    private LeadRepo leadRepo;

    public CallLog addCallLog(Long leadId, CallLog log) {

        Lead fetchedLead = leadRepo.findById(leadId.intValue()).orElseThrow(() -> new EntityNotFoundException("Lead with id "+leadId+" not found"));

        // Store the call date as per KAM time zone
        ZoneId kamZoneId = ZoneId.of(fetchedLead.getKeyAccountManager().getTimeZone());
        log.setCallDate(LocalDateTime.now(kamZoneId).truncatedTo(ChronoUnit.SECONDS));

        log.setLead(fetchedLead);

        return callLogRepo.save(log);
    }
}
