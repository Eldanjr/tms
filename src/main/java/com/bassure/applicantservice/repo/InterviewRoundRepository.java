package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.scheduling.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRoundRepository extends JpaRepository<InterviewRound,Integer> {
    
}
