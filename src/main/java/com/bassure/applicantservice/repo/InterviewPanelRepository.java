package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.scheduling.Interviewers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InterviewPanelRepository extends JpaRepository<Interviewers, Integer> {

    public List<Interviewers> findByinterviewerId(int id);

//    public List<Interviewers> findByinterviewId(InterviewSchedule id);

    @Query(value = "delete from interviewers where interview_id =:interview", nativeQuery = true)
    @Modifying
    public void deleteByinterviewId(int interview);

    @Query(value = "select interviewer_id from interview_panel  where interview_id = :id", nativeQuery = true)
    public List<Integer> findByInterviewerByinterviewId(int id);
}
