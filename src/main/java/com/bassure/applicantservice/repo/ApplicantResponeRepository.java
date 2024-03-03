package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.applicantModel.Applicant;
import com.bassure.applicantservice.model.applicantModel.ApplicantResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantResponeRepository extends JpaRepository<ApplicantResponse, Object> {

    public ApplicantResponse findByApplicantId(Applicant applicant);

    @Query(value = "DELETE FROM applicant_response WHERE applicant_id = ?1", nativeQuery = true)
    public ApplicantResponse deleteApplicantById(int id);

    @Query(value = "SELECT * FROM applicant_response WHERE applicant_id = ?1 order by id desc limit 1", nativeQuery = true)
    public ApplicantResponse findApplicantResByAppId(int id);

    @Query(value = "SELECT * FROM applicant_response WHERE job_posting_id= :Job and applicant_id = :Id order by id desc limit 1", nativeQuery = true)
    public ApplicantResponse findApplicantResByAppJob(@Param("Id") int Id, @Param("Job") int Job);
}
