package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.feedbackform.FeedBack;
import com.bassure.applicantservice.model.scheduling.Interviewers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Integer> {

    //    @Query(value="select ip.id ,q.question,f.offered_answer,fe.description from questions as q join feedback_questions as f  on f.question_id = q.id join  feedback as fe on fe.id = f.feedback_id join interview_schedule as ip on ip.id = fe.interview_id  where ip.id = :id",nativeQuery=true)
//    public List<Object[]> getFeedBackByInterviewId(@Param("id") Integer interviewScheduleId);
    @Query(value = "SELECT i.id,i.status ,i.applicant_detail_id,a.first_name,a.middle_name,a.last_name, i.started_at,i.mode,f.description FROM interview_schedule as i join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id = ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join feedback as f on f.interview_id = i.id where i.id = :id", nativeQuery = true)
    public List<Object[]> getBasicDetailsByInterviewId(@Param("id") Integer interviewScheduleId);

    @Query(value = "SELECT  q.question,fq.offered_answer FROM interview_schedule as i join feedback as f on f.interview_id = i.id join feedback_questions as fq on fq.feedback_id = f.id join questions as q on q.id = fq.question_id where i.id = :id", nativeQuery = true)
    public List<Object[]> getTableFeedbackByFeedbackId(@Param("id") Integer interviewScheduleId);

    @Query(value = "SELECT distinct i.interview_round_id, i.id ,ar.name FROM interview_schedule as i join interview_round as ar on ar.id = i.interview_round_id join job_recruiter as jr on jr.id = i.job_recruiter_id where i.applicant_detail_id = :id and jr.job_posting_id = :jobId", nativeQuery = true)
    public List<Object[]> getInterviewRoundId(@Param("id") Integer interviewScheduleId, @Param("jobId") int jobId);

    @Query(value = "SELECT distinct ip.interviewer_id FROM interviewers as ip where ip.interview_id = :id", nativeQuery = true)
    public List<Integer> getInterviewerByPanelId(@Param("id") Integer interviewScheduleId);

    @Query(value = "select f.id,f.description,f.interview_id,f.status from feedback f join interviewers i on f.interview_id = i.interviewers where i.interviewer_id =:interviewerId and i.interview_id = :interviewId", nativeQuery = true)
    public FeedBack findByInterviewerId(int interviewerId, int interviewId);

}
