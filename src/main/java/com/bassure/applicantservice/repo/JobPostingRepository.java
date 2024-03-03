package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.JobRecruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Integer>, JpaSpecificationExecutor<JobPosting> {

    public List<JobPosting> findAllByBranchIdAndDraft(int id, boolean draft);

    public List<JobPosting> findAllByCreatedByAndDraft(int id, boolean draft);

    @Query(value = "select * from job_posting as j where j.id in(select ar.job_posting_id from applicant as a join applicant_response as ar on ar.applicant_id = a.id where a.id=:applicantId and ar.interested = true) order by j.id desc;", nativeQuery = true)
    public List<JobPosting> getJobPostingAppliedByApplicant(@Param("applicantId") int applicantId);

    @Query(value = "select count(*) from job_posting as j join job_recruiter as jr on j.id = jr.job_posting_id join interview_schedule as s on jr.id = s.job_recruiter_id where j.id = :Id and s.status = :Statusname ", nativeQuery = true)
    public int getJobTotalCountIdStatus(@Param("Id") int Id, @Param("Statusname") String Statusname);

    @Query(value = "SELECT GROUP_CONCAT(DISTINCT jr.recruiter_id) AS recruiter_ids, jp.title, jp.close_date,jp.status,jp.no_of_vaccancies FROM job_posting jp JOIN job_recruiter jr ON jp.id = jr.job_posting_id WHERE jp.status = 'INPROCESS' AND jp.draft = 1 AND jp.branch_id = :branchId GROUP BY jp.title, jp.close_date, jp.no_of_vaccancies ORDER BY jp.close_date limit 3;", nativeQuery = true)
    public List<Object> getJobsInprocess(int branchId);

    @Query(value = "SELECT COUNT(CASE WHEN a.applicant_status = 'HIRED' OR  a.applicant_status = 'OFFERACCEPTED' THEN 1 END) AS hired_count,jr.recruiter_id,jp.title,jr.status,jr.assigned_counts FROM applicant AS a JOIN applicant_response AS ar ON a.id = ar.applicant_id JOIN job_posting AS jp ON ar.job_posting_id = jp.id JOIN job_recruiter AS jr ON jp.id = jr.job_posting_id WHERE jp.id= :id and jp.branch_id= :branchId GROUP BY jr.recruiter_id,jr.id,jp.title, jr.status, jr.assigned_counts", nativeQuery = true)
    List<Object> getRecruiterJobStats(int id, int branchId);

    public JobPosting findByjobRecruiters(JobRecruiter recruiter);

    @Query(value = "select a.applicant_status , jr.recruiter_id,jr.assigned_counts  from job_recruiter as jr join applicant_details as ad on jr.id = ad.job_recruiter_id join applicant_response as ar on ad.applicant_response_id = ar.id join applicant as a on ar.applicant_id = a.id  where jr.job_posting_id = :jobId", nativeQuery = true)
    public List<Object[]> getRecruiterStatusByJobId(int jobId);

    @Query(value = "select recruiter_Id ,assigned_counts from job_recruiter where job_posting_id = :jobId", nativeQuery = true)
    public List<Object[]> findRecruitersWithAssignedCounts(int jobId);

    @Query(value = "SELECT count(distinct i.applicant_detail_id) FROM job_recruiter as jr join job_posting as jp on jr.job_posting_id = jp.id join interview_schedule as i on i.job_recruiter_id = jr.id join applicant_details as ad on i.applicant_detail_id = ad.id join applicant_response as ar on ad.applicant_response_id = ar.id join applicant as a on ar.applicant_id = a.id where jr.job_posting_id = :Id and a.applicant_status = 'HIRED' ", nativeQuery = true)
    public int getFilledJobCount(@Param("Id") int Id);

    @Query(value = "SELECT distinct i.status FROM interview_schedule as i where i.applicant_detail_id= :id and i.status in ('SCHEDULED','RESCHEDULED')", nativeQuery = true)
    public String getScheduledInterviewsIfAvailable(@Param("id") int Id);

    @Query(value = "SELECT distinct jr.job_posting_id as posting_id,jp.title ,jr.assigned_counts,(select count(*) from applicant_response as ar1 where ar1.job_posting_id = posting_id and ar1.applicant_response_status in ('HIRED')) as hired_counts FROM job_recruiter as jr join job_posting as jp on jp.id = jr.job_posting_id join applicant_response as ar on ar.job_posting_id = jp.id where jr.recruiter_id =1;", nativeQuery = true)
    public List<Object[]> getRecruiterProgress(@Param("id") int id);

    @Query(value = "SELECT" +
            "    a.applicant_status," +
            "    COUNT(*) AS status_count " +
            "FROM" +
            "    job_recruiter jr " +
            "JOIN" +
            "    applicant_details ad ON jr.id = ad.job_recruiter_id " +
            "JOIN" +
            "    applicant_response ar ON ad.applicant_response_id = ar.id " +
            "JOIN" +
            "    applicant a ON ar.applicant_id = a.id " +
            "WHERE" +
            "    jr.job_posting_id = :jobId" +
            "    AND jr.recruiter_id = :recruiterId " +
            "GROUP BY" +
            "    a.applicant_status;", nativeQuery = true)
    List<Object[]> getRecruiterProgressOnJd(int jobId, int recruiterId);

    @Query(value = "SELECT jp.title, jp.no_of_vaccancies, jp.type, jp.status, jp.location, jp.open_date, jp.close_date, jp.created_by, jr.id, jp.id, jr.recruiter_id from job_posting jp JOIN job_recruiter jr on jp.id = jr.job_posting_id where recruiter_id = :recruiterId", nativeQuery = true)
    List<Object[]> getJobByRecruiterId(int recruiterId);

    @Query(value = "SELECT" +
            "    a.first_name AS applicant_name," +
            "    a.applicant_status," +
            "    ad.resume_path," +
            "    jr.id AS job_recruiter_id," +
            "    jr.job_posting_id," +
            "    jr.recruiter_id," +
            "    ad.id AS applicant_details_id," +
            "    ir.name AS interview_round_name," +
            "    isched.feedback AS feedback," +
            "    a.id AS applicant_id, " +
            "    isched.id AS interview_id "+
            "FROM" +
            "    job_recruiter jr " +
            "JOIN" +
            "    applicant_details ad ON jr.id = ad.job_recruiter_id " +
            "JOIN" +
            "    applicant_response ar ON ad.applicant_response_id = ar.id " +
            "JOIN" +
            "    applicant a ON ar.applicant_id = a.id " +
            "LEFT JOIN" +
            "    interview_schedule isched ON ad.id = isched.applicant_detail_id " +
            "LEFT JOIN " +
            "    interview_round ir ON isched.interview_round_id = ir.id " +
            "WHERE " +
            " jr.job_posting_id = :jobId" +
            " AND " +
            "jr.recruiter_id = :recruiterId", nativeQuery = true)
    List<Object[]> getCandidatesByRecruiterIdAndJobId(int jobId, int recruiterId);


}
