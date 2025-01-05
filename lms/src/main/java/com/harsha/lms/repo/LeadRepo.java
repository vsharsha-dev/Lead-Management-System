package com.harsha.lms.repo;

import com.harsha.lms.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepo extends JpaRepository<Lead, Integer> {
    Optional<Lead> findByName(String name);

//    @Query("SELECT l FROM Lead l WHERE l.keyAccountManager.id = :kamId AND " +
//            "l.lastCallDate + INTERVAL '1 day' * l.callFrequency <= :currentDate")
//    List<Lead> findLeadsRequiringCallsToday(@Param("kamId") Long kamId,
//                                            @Param("currentDate") LocalDateTime currentDate);

}
