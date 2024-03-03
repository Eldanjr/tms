
package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.OtpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OtpInfo, Integer> {


    OtpInfo findByOtp(String token);
}
