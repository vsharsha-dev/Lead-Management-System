package com.harsha.lms.repo;

import com.harsha.lms.model.KeyAccountManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KamRepo extends JpaRepository<KeyAccountManager, Integer> {

}
