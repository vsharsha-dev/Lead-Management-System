package com.harsha.lms.repo;

import com.harsha.lms.model.CallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallLogRepo extends JpaRepository<CallLog, Integer> {
}
