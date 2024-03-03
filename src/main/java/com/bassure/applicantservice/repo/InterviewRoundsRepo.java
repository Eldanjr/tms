package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.scheduling.InterviewRound;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InterviewRoundsRepo extends JpaRepository<InterviewRound, Integer> {
    
    @Query(value = "SELECT * FROM interview_round where id not in (SELECT i.interview_round_id FROM interview_schedule as i join interview_round as ir on ir.id = i.interview_round_id join job_recruiter as jr on jr.id= i.job_recruiter_id where i.applicant_detail_id = :applicantId and jr.job_posting_id = :jobId)",nativeQuery = true)
    List<InterviewRound> findByinterviewRounds(int applicantId,int jobId);
}
