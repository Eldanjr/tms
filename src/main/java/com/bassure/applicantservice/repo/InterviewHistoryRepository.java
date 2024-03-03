package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.scheduling.InterviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewHistoryRepository extends JpaRepository<InterviewHistory, Integer> {
}
