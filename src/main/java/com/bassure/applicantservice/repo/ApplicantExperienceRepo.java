
package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.applicantModel.ApplicantExperienceDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantExperienceRepo  extends JpaRepository<ApplicantExperienceDetails, Integer>{
    
        @Query(value = "SELECT * FROM applicant_experience_details where applicant_id = :id",nativeQuery = true)
    public List<ApplicantExperienceDetails> findByApplicantId(@Param("id") int id);
}
