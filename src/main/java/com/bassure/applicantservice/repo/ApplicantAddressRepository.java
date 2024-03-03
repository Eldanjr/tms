
package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.applicantModel.ApplicantAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ApplicantAddressRepository  extends JpaRepository<ApplicantAddress, Integer>{
    
}
