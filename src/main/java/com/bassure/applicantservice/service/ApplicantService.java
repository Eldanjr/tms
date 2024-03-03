package com.bassure.applicantservice.service;

import com.bassure.applicantservice.request.ApplicantAssociate;
import com.bassure.applicantservice.request.ApplicantDetailUpdateRequest;
import com.bassure.applicantservice.request.ApplicantDetailsRequest;
import com.bassure.applicantservice.request.ApplicantRequest;
import com.bassure.applicantservice.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface ApplicantService {

    public ApiResponse viewAllApplicant(Pageable pageable);

    public ApiResponse addApplicantDetails(ApplicantDetailsRequest applicantDetail);

    public ApiResponse addApplicant(ApplicantRequest applicant, boolean email);

    public ApiResponse updateApplicant(ApplicantDetailUpdateRequest applicant, int id);

    public ApiResponse updateApplicantBasic(ApplicantRequest applicant, int id);

    public ApiResponse viewSingleApplicant(int id);

    public ApiResponse applicantStatusChange(int id, String status);

    public ApiResponse applicantStatusCount(int id);

    public ApiResponse verifyApplicant(String email, String token);

    public ApiResponse findRecruiterJob(int d);

    public ApiResponse findApplicantByStatus(String sts);

    public ApiResponse findApplicantByMobile(String mob);

    public ApiResponse findApplicantByJob(int job);

    public ApiResponse findApplicantByJob1();

    public ApiResponse findApplicantByEmail(String email);

    public ApiResponse viewSingleApplicants(int id);

    public ApiResponse findApplicantBySkill(int id);

    public ApiResponse findApplicantsCount();

    public ApiResponse associate(ApplicantAssociate applicant);

    ApiResponse applicantStat(int jobRecruiter);
    
}
