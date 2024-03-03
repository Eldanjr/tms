package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.applicantModel.Applicant;
import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface ApplicantDetailsRepository extends JpaRepository<ApplicantDetails, Integer> {

    @Query("SELECT app FROM Applicant app WHERE app.contactNo = :contact ")
    Applicant findBycontactNoOrEmail(String contact);

    //    Optional<ApplicantDetails> findApplicantByApplicantId(Applicant applicantId);
    @Query(value = "SELECT * FROM applicant_details WHERE applicant_id=:id", nativeQuery = true)
    ApplicantDetails findByApplicantId1(int id);

    //    @Query(value = "SELECT * FROM applicant_details as ad JOIN applicant_response as ar ON ad.applicant_response_id = ar.id JOIN applicant as a ON ar.applicant_id = a.id WHERE a.id =:appid", nativeQuery = true)
//    Optional<ApplicantDetails> findApplicantByApplicantId(int appid);
    @Query(value = "SELECT * FROM applicant_details as ad JOIN applicant_response as ar ON ad.applicant_response_id = ar.id JOIN applicant as a ON ar.applicant_id = a.id WHERE a.id =:appid", nativeQuery = true)
    List<Object[]> findApplicantByApplicantId(int appid);

    @Query(value = "SELECT * FROM applicant_details where applicant_response_id =:id", nativeQuery = true)
    ApplicantDetails findByApplicantResponseId(int id);

    @Query(value = "select a.id,a.first_name,a.middle_name,a.last_name,a.contact_no,a.email,a.applicant_status,a.applicant_flag,ar.job_posting_id,ad.added_by,ad.id from applicant as a join applicant_response as ar join applicant_details as ad join applicant_skill as ask where ar.applicant_id=a.id and ad.applicant_response_id=ar.id and ad.id=ask.applicant_id and ask.skill_id= :skillId", nativeQuery = true)
    List<Object[]> findBySkillId(int skillId);

    @Query(value = "select ad.gender,ad.expected_ctc,ad.date_of_birth,ad.added_by,ad.added_at,ad.source,ad.reference_by,ad.current_ctc,ad.current_company,ad.reason_for_change,ad.holding_offer,ad.marital_status,ad.current_address_id,ad.resume_path,ad.year_of_experience,ad.job_recruiter_id,ad.applicant_response_id  from applicant_details as ad where ad.id = :aid", nativeQuery = true)
    List<Object[]> findByAppId(int aid);

    @Query(value = "select count(a.applicant_status) from applicant_details as ad join applicant_response as ar join applicant as a where ad.applicant_response_id=ar.id and ar.applicant_id = a.id and a.applicant_status='HIRED' and ad.job_recruiter_id=:id", nativeQuery = true)
    List<Integer> findByJobRecruiterId(int id);

    @Query(value = "SELECT a.first_name, ir.name AS level, a.applicant_status, ad.resume_path\n"
            + "FROM applicant AS a\n"
            + "JOIN applicant_response ar ON a.id = ar.applicant_id\n"
            + "JOIN interview_schedule it ON a.id = it.applicant_detail_id\n"
            + "JOIN applicant_details ad ON ad.id = it.applicant_detail_id\n"
            + "JOIN interview_round ir ON ir.id = it.interview_round_id\n"
            + "JOIN (\n"
            + "  SELECT applicant_detail_id, MAX(ir.id) AS max_level\n"
            + "  FROM interview_schedule AS it\n"
            + "  JOIN interview_round ir ON ir.id = it.interview_round_id\n"
            + "  WHERE it.job_recruiter_id = :jobRecruiterId\n"
            + "  GROUP BY applicant_detail_id\n"
            + ") max_levels ON it.applicant_detail_id = max_levels.applicant_detail_id AND ir.id = max_levels.max_level\n"
            + "WHERE it.job_recruiter_id = :jobRecruiterId\n", nativeQuery = true)
    List<Object[]> getApplicantStatusfromJrId(int jobRecruiterId);

    @Query(value = "SELECT distinct a.id,a.first_name,a.middle_name,a.last_name,a.contact_no,ar.applicant_response_status,ad.resume_path,ad.id, ad.added_by FROM  applicant_details as ad join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = ar.job_posting_id join job_recruiter as jr on jr.job_posting_id = jp.id where ar.job_posting_id = (:jobId) and ar.interested = 1 and ar.applicant_response_status in ('NEW','INPROCESS') order by ad.id desc", nativeQuery = true)
    List<Object[]> getApplicantByJobid(@Param("jobId") int jobId);

    @Query(value = "SELECT distinct a.id,a.first_name,a.middle_name,a.last_name,a.contact_no,ar.applicant_response_status,ad.resume_path,ad.id, ad.added_by FROM  applicant_details as ad join applicant_response as ar on ar.id=ad.applicant_response_id join applicant as a on a.id = ar.applicant_id join job_posting as jp on jp.id = ar.job_posting_id join job_recruiter as jr on jr.job_posting_id = jp.id where ar.job_posting_id = (:jobId) and ar.interested = 1 and ar.applicant_response_status in (:status) order by ad.id desc", nativeQuery = true)
    List<Object[]> getApplicantByJobidAndStatus(@Param("jobId") int jobId, @Param("status") String status);

//   ,ad.date_of_birth,ad.added_by,ad.added_at,ad.source,ad.reference_by,ad.current_ctc,ad.current_company,ad.reason_for_change,ad.holding_offer,ad.marital_status,ad.current_address_id,ad.resume_path,ad.year_of_experience,ad.job_recruiter_id,ad.applicant_response_id
    @Query(value = "SELECT distinct jr.recruiter_id as ri,(select sum(jr1.assigned_counts) from job_recruiter as jr1 where jr1.recruiter_id = ri  and jr1.job_posting_id = :jobId) as assigned_counts,(select count(*) from applicant_response as ar1 where ar1.job_posting_id = jr.job_posting_id and ar1.applicant_response_status in ('HIRED')) as hired_counts FROM job_recruiter as jr join job_posting as jp on jp.id = jr.job_posting_id join applicant_response as ar on ar.job_posting_id = jp.id where jr.recruiter_id in (:recuriters) and jp.id = :jobId", nativeQuery = true)
    public List<Object[]> getRecuriterStaticsByJobId(@Param("recuriters") Object recuriterIds, @Param("jobId") int jobId);

    @Query(value = "SELECT distinct jr.recruiter_id as ri,(select sum(jr1.assigned_counts) from job_recruiter as jr1 where jr1.recruiter_id = ri) as assigned_counts,(select count(*) from applicant_response as ar1 where ar1.job_posting_id = jr.job_posting_id and ar1.applicant_response_status in ('HIRED')) as hired_counts FROM job_recruiter as jr join job_posting as jp on jp.id = jr.job_posting_id join applicant_response as ar on ar.job_posting_id = jp.id where jr.recruiter_id in (:recuriters) order by RAND() limit 3;", nativeQuery = true)
    public List<Object[]> getRandomRecuriterStatics(@Param("recuriters") Object recuriterIds);

    @Query(value = "select ar.applicant_response_status from applicant_response as ar where ar.applicant_id = :applicantId and ar.job_posting_id = :jobId", nativeQuery = true)
    public String getApplicantStatusByJob(@Param("jobId") int jobId, @Param("applicantId") int applicantId);

}
