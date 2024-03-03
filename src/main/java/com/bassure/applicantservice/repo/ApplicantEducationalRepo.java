
package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.applicantModel.ApplicantEducationalDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantEducationalRepo extends JpaRepository<ApplicantEducationalDetails, Integer>{

    @Query(value = "SELECT * FROM applicant_educational_details where applicant_id = :id",nativeQuery = true)
    public List<ApplicantEducationalDetails> findByApplicantId(@Param("id") int id);
    
}
