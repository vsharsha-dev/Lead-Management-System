package com.harsha.lms.repo;

import com.harsha.lms.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadRepo extends JpaRepository<Lead, Integer> {
    Optional<Lead> findByName(String name);

}
