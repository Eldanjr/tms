package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.JobTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTemplateRepository extends JpaRepository<JobTemplate, Integer> {
    
}
