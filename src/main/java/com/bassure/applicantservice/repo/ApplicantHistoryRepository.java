
package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.applicantModel.ApplicantHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantHistoryRepository extends JpaRepository<ApplicantHistory, Integer>{
    
}
