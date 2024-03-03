package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Integer>, JpaSpecificationExecutor<InterviewSchedule> {

    @Query(value = "select * from interview_schedule as ins join interviewers as ip on ins.id = ip.interview_id where interviewer_id =:interviewerId and started_at <=:to and ended_at >=:from", nativeQuery = true)
    public InterviewSchedule findbystartedAtANDendedAt(int interviewerId, Timestamp from, Timestamp to);

    public List<InterviewSchedule> findByjobRecruiterId(int recruiter);

    //
    @Query(value = "select p.id,p.title,p.description,p.experience,p.no_of_vaccancies,p.type,p.status,p.location,p.open_date,p.close_date,p.modified_by,p.modified_at from job_posting as p join job_recruiter as r on p.id = r.job_posting_id where r.id= :id", nativeQuery = true)
    public List<Object[]> findJobByRecuriterId(@Param("id") int id);

    @Query(value = "select i.id , i.interview_round_id ,i.applicant_detail_id , i.mode , i.status,i.feedback , i.started_at , i.ended_at ,i.location from interview_schedule as i where job_recruiter_id = :id", nativeQuery = true)
    public List<Object[]> findSecheduledInterviewsByJobRecruiterId(int id);

    @Query(value = "SELECT * FROM interview_schedule where id in (SELECT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id where r.recruiter_id = :recruiter)", nativeQuery = true)
    public List<InterviewSchedule> findjobScheduleRecruiterId(@Param("recruiter") int recruiter);

    @Query(value = "SELECT * FROM interview_schedule where id in (SELECT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id where r.recruiter_id in (:recuriterIds));", nativeQuery = true)
    public List<InterviewSchedule> findingInterviewbyRecuriterIds(@Param("recuriterIds") Object ids);

    @Query(value = "select i.interviewer_id from interviewers as i join interview_schedule as s on i.interview_id = s.id where s.id in(:interviewScheduleId);", nativeQuery = true)
    public List<Integer> getRecuritersIdByInterviewSchedule(@Param("interviewScheduleId") int interviewScheduleId);

    @Query(value = "SELECT DISTINCT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id where r.recruiter_id in (:recuritersId) order by i.id desc  limit 1", nativeQuery = true)
    public List<Object> getApplicantIdshandledByRecuriters(@Param("recuritersId") Object recuritersId);

    @Query(value = "SELECT i.id AS interview_schedule,ir.name AS round,i.started_at,i.ended_at,GROUP_CONCAT(r.interviewer_id) AS interviewer_ids FROM interview_schedule AS i JOIN interviewers AS r ON i.id = r.interview_id JOIN job_recruiter jr ON jr.id = i.job_recruiter_id JOIN interview_round ir ON i.interview_round_id = ir.id WHERE jr.recruiter_id = :recruiterId GROUP BY i.id, ir.name, i.started_at, i.ended_at ORDER BY i.id DESC LIMIT 3", nativeQuery = true)
    public List<Object[]> getRecruiterInterviewProgress(int recruiterId);

    //
    @Query(value = "SELECT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id where r.recruiter_id in (:recuritersId) and i.status not in ('SELECTED','COMPLETED','CANCELLED') ;", nativeQuery = true)
    public List<Object> getApplicantIdshandledByRecuritersWithStatus(@Param("recuritersId") Object recuritersId);

    //
    @Query(value = "SELECT DISTINCT i.applicant_detail_id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id where r.recruiter_id in (:recuritersId)", nativeQuery = true)
    public List<Object> getAppIdOfLastStatus(@Param("recuritersId") Object recuritersId);

    //
    @Query(value = "SELECT DISTINCT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id where r.recruiter_id = :recuritersId", nativeQuery = true)
    public List<Object> getApplicantIdhandledByRecuriters(@Param("recuritersId") int recuritersId);

    //
    @Query(value = "SELECT * FROM interview_schedule where id in (SELECT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id = ad.applicant_response_id join applicant as a on a.id = ar.applicant_id where a.id = :applicantId and r.job_posting_id = :jobPostingId) order by started_at desc", nativeQuery = true)
    public List<InterviewSchedule> getInterviewByApplicantIdandJobPostingId(@Param("applicantId") int applicantId, @Param("jobPostingId") int jobPostingId);

    @Query(value = "SELECT * FROM interview_schedule as i where i.status = :interviewStatus and i.id in (SELECT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id = ad.applicant_response_id join applicant as a on a.id = ar.applicant_id where a.id = :applicantId and r.job_posting_id = :jobPostingId) order by i.started_at desc", nativeQuery = true)
    public List<InterviewSchedule> getInterviewByApplicantIdandJobPostingIdWithInterviewStatus(@Param("applicantId") int applicantId, @Param("jobPostingId") int jobPostingId, @Param(value = "interviewStatus") String interviewStatus);

    @Query(value = "SELECT i.id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id where i.applicant_detail_id = :applicantId and r.job_posting_id = :jobPostingId;", nativeQuery = true)
    public List<Integer> gettingInterviewIds(@Param("applicantId") int applicantId, @Param("jobPostingId") int jobPostingId);

    //
    @Query(value = "select DISTINCT i.applicant_detail_id from interview_schedule as i join job_recruiter as j on i.job_recruiter_id=j.id where j.job_posting_id = :jobPostingId and recruiter_id in (:jobRecruiterId);", nativeQuery = true)
    public Set<Integer> interScheduleDetails(@Param(value = "jobRecruiterId") Object jobRecruiterId, @Param(value = "jobPostingId") int jobPostingId);

    //
    @Query(value = "SELECT id FROM interview_schedule where applicant_detail_id = :id order by id desc limit 1 ;", nativeQuery = true)
    public Object getLastStatusOfApplicant(@Param("id") Object id);

    //
    @Query(value = "SELECT id FROM interview_schedule where id in (:applicantId) and status = :status ;", nativeQuery = true)
    public List<Object> getInterviewByStatus(@Param("applicantId") Object applicantId, @Param("status") String status);

    @Query(value = "SELECT i.applicant_detail_id,a.first_name,a.middle_name,a.last_name,a.applicant_status,ad.resume_path,a.email,a.contact_no,i.started_at,jr.job_posting_id as job_id,jr.recruiter_id  as Recuriter_id, jp.title  as job_title,i.id  as interview_id,i.status as interview_status,ir.name as round_name FROM interview_schedule as i join job_recruiter as jr on i.job_recruiter_id = jr.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = jr.job_posting_id join interview_round as ir on ir.id = i.interview_round_id where jr.recruiter_id in (:recuritersIds) and i.status in (:interviewStatus) order by i.id desc", nativeQuery = true)
    public List<Object[]> getScheduledApplicants(@Param("recuritersIds") Object recuriterIds, @Param("interviewStatus") List<String> interviewStatus, Pageable pageable);

    @Query(value = "SELECT count(*) FROM interview_schedule as i join job_recruiter as jr on i.job_recruiter_id = jr.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = jr.job_posting_id join interview_round as ir on ir.id = i.interview_round_id where jr.recruiter_id in (:recuritersIds) and i.status in (:interviewStatus) order by i.id desc", nativeQuery = true)
    public int getScheduledApplicantsCount(@Param("recuritersIds") Object recuriterIds, @Param("interviewStatus") List<String> interviewStatus);

    @Query(value = "SELECT distinct i.applicant_detail_id,a.first_name,a.middle_name,a.last_name,a.applicant_status,ad.resume_path,a.email,a.contact_no,jr.recruiter_id FROM interview_schedule as i join job_recruiter as jr on i.job_recruiter_id = jr.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = jr.job_posting_id where jr.job_posting_id = :jobId and a.applicant_status in ('NEW','INPROCESS','OFFERED','OFFERACCEPTED')", nativeQuery = true)
    public List<Object[]> getApplicantsByJob(@Param("jobId") int jobId);

    @Query(value = "SELECT i.applicant_detail_id,a.first_name,a.middle_name,a.last_name,a.applicant_status,ad.resume_path,a.email,a.contact_no ,i.started_at,jr.job_posting_id as job_id,jr.recruiter_id  as Recuriter_id, jp.title  as job_title,i.id  as interview_id,i.status as interview_status,ir.name as round_name FROM interview_schedule as i join job_recruiter as jr on i.job_recruiter_id = jr.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = jr.job_posting_id join interview_round as ir on ir.id = i.interview_round_id where jr.recruiter_id in (:recuritersIds) and i.status in (:interviewStatus) and i.started_at >= :startDate and i.started_at <= :endDate order by i.id desc ", nativeQuery = true)
    public List<Object[]> getScheduledApplicantsWithDate(@Param("recuritersIds") Object recuriterIds, @Param("interviewStatus") List<String> interviewStatus, @Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    @Query(value = "SELECT count(*) FROM interview_schedule as i join job_recruiter as jr on i.job_recruiter_id = jr.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = jr.job_posting_id join interview_round as ir on ir.id = i.interview_round_id where jr.recruiter_id in (:recuritersIds) and i.status in (:interviewStatus) and i.started_at >= :startDate and i.started_at <= :endDate order by i.id desc ", nativeQuery = true)
    public int getScheduledApplicantsWithDateCount(@Param("recuritersIds") Object recuriterIds, @Param("interviewStatus") List<String> interviewStatus, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select * from interview_schedule where id in (select i.id from interview_schedule i join interviewers ip on i.id = ip.interview_id  where ip.interviewer_id =:id) order by started_at desc; ", nativeQuery = true)
    public List<InterviewSchedule> getinterviewbyinterviewer(int id);

    @Query(value = "SELECT distinct r.recruiter_id FROM interview_schedule as i join job_recruiter as r on i.job_recruiter_id = r.id join applicant_details as ad on ad.id = i.applicant_detail_id where i.applicant_detail_id = :applicantId and r.job_posting_id = :jobId limit 1", nativeQuery = true)
    public int getRecuriterIdByApplicantDetailsId(@Param("applicantId") int id, @Param("jobId") int jobId);

    @Query(value = "SELECT i.applicant_detail_id,a.first_name,a.middle_name,a.last_name, jp.title  as job_title, ad.year_of_experience FROM interview_schedule as i join job_recruiter as jr on i.job_recruiter_id = jr.id join applicant_details as ad on ad.id = i.applicant_detail_id join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = jr.job_posting_id join interview_round as ir on ir.id = i.interview_round_id where jr.recruiter_id in (:recuritersIds) and i.status in (:interviewStatus) order by RAND() limit 2", nativeQuery = true)
    public List<Object[]> getRandomApplicantByBranch(@Param("recuritersIds") Object recuriterIds, @Param("interviewStatus") List<String> interviewStatus);

    @Query(value = "select * from interview_schedule where id in (select i.id from interview_schedule i join interviewers ip on i.id = ip.interview_id  where i.id =:id)", nativeQuery = true)
    public InterviewSchedule getInterviewDetails(int id);
}
