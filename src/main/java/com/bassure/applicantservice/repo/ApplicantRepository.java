package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.applicantModel.Applicant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Integer>, JpaSpecificationExecutor<Applicant> {

    Applicant findByContactNo(String Contact);

//   @Query("SELECT app FROM Applicant app WHERE app.contactNo = :contact ")
//      Applicant findBycontactNoOrEmail(String contact);
    Applicant findByEmail(String email);

    @Query(value = "SELECT * FROM applicant WHERE applicant_status NOT IN ('HIRED', 'OFFERREJECTED', 'DECLINED','REJECTED') order by id desc ", nativeQuery = true)
    List<Applicant> findActiveApplicant1(Pageable pageable);

    @Query(value = "SELECT * FROM applicant WHERE applicant_status = :sts", nativeQuery = true)
    public List<Applicant> findApplicantByStatus(String sts);

    @Query(value = "SELECT * FROM applicant WHERE applicant_status NOT IN ('HIRED', 'OFFERREJECTED', 'DECLINED','REJECTED') order by id desc ", nativeQuery = true)
    List<Applicant> findActiveApplicant();

    @Query(value = "SELECT count(id) FROM applicant WHERE applicant_status NOT IN ('HIRED', 'OFFERREJECTED', 'DECLINED','REJECTED') order by id desc ", nativeQuery = true)
    int findActiveApplicantsCount();
}
